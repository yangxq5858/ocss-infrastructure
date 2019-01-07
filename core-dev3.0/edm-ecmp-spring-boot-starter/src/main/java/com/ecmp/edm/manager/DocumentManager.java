package com.ecmp.edm.manager;

import com.ecmp.context.ContextUtil;
import com.ecmp.edm.entity.*;
import com.ecmp.edm.manager.dao.BusinessInfoDao;
import com.ecmp.edm.manager.dao.DocumentInfoDao;
import com.ecmp.edm.manager.dao.ThumbnailDao;
import com.ecmp.exception.ServiceException;
import com.ecmp.util.EnumUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：文档管理器
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017-07-11 16:47      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
public class DocumentManager implements IDocumentManager {
    /**
     * 文档清理的天数
     */
    private static final int CLEAR_DAYS = 180;
    /**
     * 文档暂存的天数
     */
    private static final int TEMP_DAYS = 1;

    @Autowired(required = false)
    private DocumentInfoDao documentInfoDao;
    @Autowired(required = false)
    private GridFsOperations edmGridFsTemplate;
    @Autowired(required = false)
    private ThumbnailDao thumbnailDao;
    @Autowired(required = false)
    private BusinessInfoDao businessInfoDao;
    @Autowired
    private MongoDbFactory mongoDbFactory;

    /**
     * 上传一个文档
     *
     * @param document          文档
     * @param generateThumbnail 生成缩略图
     * @return 文档信息
     */
    public DocumentInfo uploadDocument(Document document, boolean generateThumbnail) {
        if (Objects.isNull(document) || Objects.isNull(document.getInfo())) {
            return null;
        }
        InputStream stream = document.getStream();
        if (Objects.isNull(stream)) {
            return null;
        }
        //将数据流缓存
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        try {
            while ((len = stream.read(buffer)) > -1) {
                arrayOutputStream.write(buffer, 0, len);
            }
            arrayOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        DocumentInfo info = document.getInfo();
        //初始化当前环境
        initCurrentInfo(info);
        //获取文档类型
        DocumentType documentType = EnumUtils.getEnum(DocumentType.class, info.getDocumentType());
        Thumbnail thumbnail = null;
        //如果是图像文档，生成缩略图
        if (documentType == DocumentType.Image && generateThumbnail) {
            //复制数据流
            InputStream imageStream = new ByteArrayInputStream(arrayOutputStream.toByteArray());
            String suffix = FileNameUtil.getExtension(info.getFileName());
            byte[] thumbData = ImageUtil.thumbnailImage(imageStream, suffix, new ImageRectangle(150, 100));
            if (Objects.nonNull(thumbData)) {
                thumbnail = new Thumbnail();
                thumbnail.setFileName(info.getFileName());
                thumbnail.setImage(thumbData);
            }
        }
        try {
            //重置数据流
            InputStream dataStream = new ByteArrayInputStream(arrayOutputStream.toByteArray());
            //设置文件大小
            info.setSize((long) dataStream.available());
            //保存数据文件
            DBObject metaData = new BasicDBObject();
            metaData.put("description", info.getDescription());
            ObjectId objectId = edmGridFsTemplate.store(dataStream, info.getFileName(), documentType.toString(), metaData);
            info.setId(objectId.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        info = documentInfoDao.save(info);
        if (Objects.nonNull(info) && Objects.nonNull(thumbnail)) {
            thumbnail.setId(info.getId());
            thumbnailDao.save(thumbnail);
        }
        return info;
    }

    /**
     * 初始化当前环境
     *
     * @param info 文档信息
     */
    private void initCurrentInfo(DocumentInfo info) {
        info.setSize(0L);
        info.setUploadedTime(new Date());
        info.setAppModule(ContextUtil.getAppCode());
        info.setTenantCode(ContextUtil.getTenantCode());
        info.setUploadUserId(ContextUtil.getUserId());
        info.setUploadUserAccount(ContextUtil.getUserAccount());
        info.setUploadUserName(ContextUtil.getUserName());
        info.setDocumentType(FileNameUtil.getDocumentType(info.getFileName()).ordinal());
    }

    /**
     * 上传一个文档(如果是图像生成缩略图)
     *
     * @param document 文档
     * @return 文档信息
     */
    @Override
    public DocumentInfo uploadDocument(Document document) {
        return uploadDocument(document, true);
    }

    /**
     * 上传一个文档(如果是图像生成缩略图)
     *
     * @param stream   文档数据流
     * @param fileName 文件名
     * @return 文档信息
     */
    @Override
    public DocumentInfo uploadDocument(InputStream stream, String fileName) {
        DocumentInfo info = new DocumentInfo();
        info.setFileName(fileName);
        Document document = new Document(info, stream);
        return uploadDocument(document);
    }

    /**
     * 获取一个文档(包含信息和数据)
     *
     * @param id          文档Id
     * @param isThumbnail 是获取缩略图
     * @return 文档
     */
    @Override
    public Document getDocument(String id, boolean isThumbnail) {
        Optional<DocumentInfo> optional = documentInfoDao.findById(id);
        if (!optional.isPresent()) {
            return null;
        }
        DocumentInfo info = optional.get();
        InputStream stream;
        //获取缩略图
        if (isThumbnail) {
            Optional<Thumbnail> optional1 = thumbnailDao.findById(id);
            if (!optional1.isPresent()) {
                return new Document(info, null);
            }
            stream = new ByteArrayInputStream(optional1.get().getImage());
            return new Document(info, stream);
        }
        //获取原图
        GridFSFile fsdbFile = edmGridFsTemplate.findOne(new Query().addCriteria(Criteria.where("_id").is(id)));
        if (Objects.isNull(fsdbFile)) {
            return new Document(info, null);
        }
        GridFSBucket bucket = GridFSBuckets.create(mongoDbFactory.getDb());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bucket.downloadToStream(fsdbFile.getId(), baos);
        return new Document(info, new ByteArrayInputStream(baos.toByteArray()));
    }

    /**
     * 获取一个文档(包含信息和数据)
     *
     * @param id 文档Id
     * @return 文档
     */
    @Override
    public Document getDocument(String id) {
        return getDocument(id, false);
    }

    /**
     * 提交业务实体的文档信息
     *
     * @param entityId    业务实体Id
     * @param documentIds 文档Id清单
     */
    @Override
    public void submitBusinessInfos(String entityId, Collection<String> documentIds) {
        //如果文档Id清单为空，不执行操作
        if (Objects.isNull(documentIds)) {
            return;
        }
        //先移除现有业务信息
        List<BusinessInfo> infos = businessInfoDao.findAllByEntityId(entityId);
        if (Objects.nonNull(infos) && !infos.isEmpty()) {
            businessInfoDao.deleteAll(infos);
        }
        //插入文档信息
        for (String docId : documentIds) {
            BusinessInfo info = new BusinessInfo(entityId, docId);
            businessInfoDao.save(info);
        }
    }

    /**
     * 删除业务实体的文档信息
     *
     * @param entityId 业务实体Id
     */
    @Override
    public void deleteBusinessInfos(String entityId) {
        submitBusinessInfos(entityId, new ArrayList<>());
    }

    /**
     * 获取业务实体的文档信息清单
     *
     * @param entityId 业务实体Id
     * @return 文档信息清单
     */
    @Override
    public List<DocumentInfo> getEntityDocumentInfos(String entityId) {
        List<BusinessInfo> businessInfos = businessInfoDao.findAllByEntityId(entityId);
        List<DocumentInfo> result = new ArrayList<>();
        businessInfos.forEach((i) -> {
            if (StringUtils.isNotBlank(i.getDocumentId())) {
                Optional<DocumentInfo> optional = documentInfoDao.findById(i.getDocumentId());
                optional.ifPresent(result::add);
            }
        });
        return result;
    }

    /**
     * 清理所有文档(删除无业务信息的文档)
     */
    @Override
    public void cleanAllDocuments() {
        //获取需要清理的文档Id清单
        Date startTime = DateUtils.addDays(new Date(), 0 - CLEAR_DAYS);
        Date endTime = DateUtils.addDays(new Date(), 0 - TEMP_DAYS);
        List<DocumentInfo> documentInfos = documentInfoDao.findAllByUploadedTimeBetween(startTime, endTime);
        if (Objects.isNull(documentInfos) || documentInfos.isEmpty()) {
            return;
        }
        //获取可以删除的文档Id清单
        List<String> docIds = new ArrayList<>();
        documentInfos.forEach((d) -> {
            BusinessInfo businessInfo = businessInfoDao.findFirstByDocumentId(d.getId());
            if (Objects.isNull(businessInfo)) {
                docIds.add(d.getId());
            }
        });
        //删除业务孤立的文档
        if (docIds.isEmpty()) {
            return;
        }
        for (String id : docIds) {
            //删除文档信息
            documentInfoDao.deleteById(id);
            //删除缩略图
            thumbnailDao.deleteById(id);
            //删除文档数据
            Query query = new Query().addCriteria(Criteria.where("_id").is(id));
            edmGridFsTemplate.delete(query);
        }
    }
}
