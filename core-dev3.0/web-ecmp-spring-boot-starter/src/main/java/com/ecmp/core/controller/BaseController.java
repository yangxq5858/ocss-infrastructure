package com.ecmp.core.controller;

import com.ecmp.annotation.IgnoreCheckAuth;
import com.ecmp.core.common.DateConverter;
import com.ecmp.core.util.BytesEncodingDetectUtil;
import com.ecmp.core.util.ExcelUtils;
import com.ecmp.core.util.WordPdfUtils;
import com.ecmp.core.vo.OperateStatus;
import com.ecmp.edm.entity.Document;
import com.ecmp.edm.entity.DocumentInfo;
import com.ecmp.edm.manager.IDocumentManager;
import com.ecmp.log.util.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyEditorSupport;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * <strong>实现功能:</strong>
 * <p>控制器基类</p>
 * 上传、提交、删除、列表、获取缩略图、获取原始图
 *
 * @author <a href="mailto:chao2.ma@changhong.com">马超(Vision.Mac)</a>
 * @version 1.0.1 2017/8/3 14:02
 */
public abstract class BaseController {

    protected static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);
    /**
     * 重定向前缀
     */
    public static final String REDIRECT = "redirect:";
    /**
     * 转发前缀
     */
    public static final String FORWARD = "forward:";

    @Autowired(required = false)
    protected IDocumentManager documentManager;

    @InitBinder
    public void initListBinder(WebDataBinder binder) {
        //因为springmvc默认只支持256个对象映射，加入以下代码即可解决
        binder.setAutoGrowCollectionLimit(1024);
        //将前台传递过来的日期格式的字符串，自动转化为Date类型
        binder.registerCustomEditor(Date.class, new DateConverter());

        binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String value) {
                if (StringUtils.isNotBlank(value)) {
                    setValue(value);
                } else {
                    setValue(null);
                }
            }
        });
        binder.registerCustomEditor(Double.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String value) {
                try {
                    if (StringUtils.isNotBlank(value)) {
                        setValue((new BigDecimal(value)).doubleValue());
                    } else {
                        setValue(null);
                    }
                } catch (Exception e) {
                    setValue(null);
                }
            }
        });
        binder.registerCustomEditor(BigDecimal.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String value) {
                try {
                    if (StringUtils.isNotBlank(value)) {
                        setValue(new BigDecimal(value));
                    } else {
                        setValue(null);
                    }
                } catch (Exception e) {
                    setValue(null);
                }
            }
        });
    }

    /**
     * 文件上传
     *
     * @param request request
     * @return 返回上传成功页面地址
     * @throws IllegalStateException IllegalStateException
     * @throws IOException           IOException
     */
    @ResponseBody
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public final List<String> upload(MultipartHttpServletRequest request) throws IllegalStateException, IOException {
        long startTime = System.currentTimeMillis();
        MultiValueMap<String, MultipartFile> files = request.getMultiFileMap();
        List<String> docList = new ArrayList<String>();
        if (files != null) {
            for (List<MultipartFile> list : files.values()) {
                MultipartFile file = list.get(0);
                //一次遍历所有文件
                if (file != null && !file.isEmpty()) {
                    //保存上传文件
                    docList.add(doSaveUploadFile(file, request));
                }
            }
        }
        long endTime = System.currentTimeMillis();
        LOGGER.debug("文件上传耗时：" + String.valueOf(endTime - startTime) + "ms");
        return docList;
    }

    /**
     * 保存上传文件.
     * 子类重写
     *
     * @param file    上传的文件
     * @param request 文件上传请求
     */
    protected String doSaveUploadFile(final MultipartFile file, HttpServletRequest request) {
        DocumentInfo documentInfo = null;
        try {
            documentInfo = documentManager.uploadDocument(file.getInputStream(), file.getOriginalFilename());
        } catch (IOException e) {
            LogUtil.error("文件上传失败", e);
        }
        return Objects.nonNull(documentInfo) ? documentInfo.getId() : null;
    }

    /**
     * 获取文档
     *
     * @param docId       文档id
     * @param isThumbnail 是否是获取缩略图
     * @param request     请求
     * @param response    响应
     */
    @IgnoreCheckAuth
    @RequestMapping(value = "download")
    public final void download(@RequestParam(value = "docId") String docId,
                               @RequestParam(value = "isThumbnail", defaultValue = "false") Boolean isThumbnail,
                               HttpServletRequest request, HttpServletResponse response) {
        downloadDocument(docId, isThumbnail, request, response);
    }

    /**
     * 获取文档.
     * 子类重写
     *
     * @param docId       文档id
     * @param isThumbnail 是否是获取缩略图
     * @param request     请求
     * @param response    响应
     */
    protected void downloadDocument(String docId, boolean isThumbnail,
                                    HttpServletRequest request, HttpServletResponse response) {
        Document document = documentManager.getDocument(docId, isThumbnail);
        if (Objects.nonNull(document)) {
            //清空response
            response.reset();
            // 设置强制下载不打开
//            response.setContentType("application/force-download");
            response.setContentType("application/octet-stream");
            // 设置文件名
            try {
                String fileName = document.getInfo().getFileName();
                /*
                 * IE，通过URLEncoder对filename进行UTF8编码
                 * 其他的浏览器（firefox、chrome、safari、opera），则要通过字节转换成ISO8859-1
                 */
                if (StringUtils.containsAny(request.getHeader("User-Agent").toUpperCase(), "MSIE", "EDGE")) {
                    fileName = URLEncoder.encode(fileName, "UTF-8");
                } else {
                    fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
                }
                response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
            } catch (UnsupportedEncodingException e) {
                LogUtil.error("文件名编码错误", e);
            }
            byte[] buffer = new byte[2048];
            InputStream is = document.getStream();
            BufferedInputStream bis = null;
            try {
                bis = new BufferedInputStream(is);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
            } catch (IOException e) {
                LogUtil.error("文件下载错误", e);
            } finally {
                if (Objects.nonNull(bis)) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 预览文档
     *
     * @param docId       文档id
     * @param isThumbnail 是否是获取缩略图
     * @param request     请求
     * @param response    响应
     */
    @IgnoreCheckAuth
    @RequestMapping(value = "preview")
    public final void preview(@RequestParam(value = "docId") String docId,
                              @RequestParam(value = "isThumbnail", defaultValue = "false") Boolean isThumbnail,
                              HttpServletRequest request, HttpServletResponse response) {
        previewDocument(docId, isThumbnail, request, response);
    }

    /**
     * 预览文档.
     * 子类重写
     *
     * @param docId       文档id
     * @param isThumbnail 是否是获取缩略图
     * @param request     请求
     * @param response    响应
     */
    protected void previewDocument(String docId, boolean isThumbnail,
                                   HttpServletRequest request, HttpServletResponse response) {
        Document document = documentManager.getDocument(docId, isThumbnail);
        if (Objects.nonNull(document)) {
            //清空response
            response.reset();
            DocumentInfo documentInfo = document.getInfo();
            //文件后缀
            String fileName = documentInfo.getFileName();
            int dotIndex = fileName.lastIndexOf(".");
            String prefix = fileName.substring(0, dotIndex);
            String suffix = fileName.substring(dotIndex + 1);
            switch (documentInfo.getDocumentTypeEnum()) {
                case Pdf:
                    response.setContentType("application/pdf");
                    break;
                case Word:
                    //如果是docx
                    if (suffix.equalsIgnoreCase("docx")) {
                        response.setContentType("application/pdf");
                        fileName = prefix + ".pdf";
                    }
                    if (suffix.equalsIgnoreCase("doc")) {
                        response.setContentType("text/html;charset=utf-8");
                    }
                    break;
                case Excel:
                    response.setContentType("text/html;charset=utf-8");
                    break;
                case Other:
                    if (suffix.equalsIgnoreCase("txt")) {
                        response.setContentType("text/html;charset=utf-8");
                    }
                    break;
                default:

            }
            // 设置文件名
            try {
                /*
                 * IE，通过URLEncoder对filename进行UTF8编码
                 * 其他的浏览器（firefox、chrome、safari、opera），则要通过字节转换成ISO8859-1
                 */
                if (StringUtils.containsAny(request.getHeader("User-Agent").toUpperCase(), "MSIE", "EDGE")) {
                    fileName = URLEncoder.encode(fileName, "UTF-8");
                } else {
                    fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
                }
                response.setHeader("Content-Disposition", "fileName=" + fileName);
            } catch (UnsupportedEncodingException e) {
                LogUtil.error("文件名编码错误", e);
            }

            InputStream is = document.getStream();
            switch (documentInfo.getDocumentTypeEnum()) {
                case Word:
                    if (suffix.equalsIgnoreCase("docx")) {
                        try {
                            OutputStream out = response.getOutputStream();
                            //3.通过response获取ServletOutputStream对象(out)
                            out = response.getOutputStream();
                            WordPdfUtils.wordConverterToPdf(is, out);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (Objects.nonNull(is)) {
                                try {
                                    is.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    if (suffix.equalsIgnoreCase("doc")) {
                        BufferedInputStream bis = new BufferedInputStream(is);
                        try {
                            WordExtractor ex = new WordExtractor(bis);
                            String bodyText = ex.getText();
                            StringBuilder sb = new StringBuilder();
                            sb.append("<pre style='height:100%;overflow:auto;'>").append(bodyText).append("</pre>");
                            response.getWriter().write(sb.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (Objects.nonNull(bis)) {
                                try {
                                    bis.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    break;
                case Excel:
                    try {
                        String content = ExcelUtils.ExcelToHtml(is);
                        StringBuilder sb = new StringBuilder();
                        sb.append("<pre style='height:100%;overflow:auto;'>").append(content).append("</pre>");
                        response.getWriter().write(sb.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                        if (Objects.nonNull(is)) {
                            try {
                                is.close();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                    break;
                case Other:
                    if (suffix.equalsIgnoreCase("txt")) {
                        try {
                            BytesEncodingDetectUtil bytesEncodingDetectUtil = new BytesEncodingDetectUtil();
                            byte[] rawtext = bytesEncodingDetectUtil.getBytes(document.getStream());
                            String code = BytesEncodingDetectUtil.javaname[bytesEncodingDetectUtil.detectEncoding(rawtext)];
                            String res = new String(rawtext, code);
                            PrintWriter writer = response.getWriter();
                            writer.write("<pre style='height:100%;overflow:auto;'>");
                            writer.write(res + "\r\n");
                            writer.write("</pre>");
                        } catch (IOException e) {
                            LogUtil.error("文件解析错误", e);
                        }
                    } else {
                        BufferedInputStream bis = null;
                        try {
                            bis = new BufferedInputStream(is);
                            OutputStream os = response.getOutputStream();
                            byte[] buffer = new byte[2048];
                            int i = bis.read(buffer);
                            while (i != -1) {
                                os.write(buffer, 0, i);
                                i = bis.read(buffer);
                            }
                        } catch (IOException e) {
                            LogUtil.error("文件下载错误", e);
                        } finally {
                            if (Objects.nonNull(bis)) {
                                try {
                                    bis.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    break;
                default:
                    BufferedInputStream bis = null;
                    try {
                        bis = new BufferedInputStream(is);
                        OutputStream os = response.getOutputStream();
                        byte[] buffer = new byte[2048];
                        int i = bis.read(buffer);
                        while (i != -1) {
                            os.write(buffer, 0, i);
                            i = bis.read(buffer);
                        }
                    } catch (IOException e) {
                        LogUtil.error("文件下载错误", e);
                    } finally {
                        if (Objects.nonNull(bis)) {
                            try {
                                bis.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    break;
            }
        }
    }

    /**
     * 获取业务实体的文档信息清单
     *
     * @param entityId 业务实体Id
     * @param request  请求
     * @return 文档信息清单json
     */
    @IgnoreCheckAuth
    @ResponseBody
    @RequestMapping(value = "getEntityDocumentInfos")
    public final OperateStatus getEntityDocumentInfos(@RequestParam(value = "entityId") String entityId,
                                                      HttpServletRequest request) {
        OperateStatus status = OperateStatus.defaultSuccess();
        List<DocumentInfo> documentInfoList = documentManager.getEntityDocumentInfos(entityId);
        if (Objects.nonNull(documentInfoList) && !documentInfoList.isEmpty()) {
            status.setData(documentInfoList);
        }
        return status;
    }

}
