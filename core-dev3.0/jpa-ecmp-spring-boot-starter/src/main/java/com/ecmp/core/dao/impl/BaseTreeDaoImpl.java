package com.ecmp.core.dao.impl;

import com.ecmp.context.ContextUtil;
import com.ecmp.core.dao.BaseTreeDao;
import com.ecmp.core.dao.jpa.impl.BaseDaoImpl;
import com.ecmp.core.entity.*;
import com.ecmp.core.search.Search;
import com.ecmp.core.search.SearchFilter;
import com.ecmp.core.search.SearchOrder;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 实现功能:
 * 树形实体Dao基类
 *
 * @param <T> BaseEntity的子类
 * @author 马超(Vision.Mac)
 * @version 1.0.1 2017/5/9 1:38
 */
public class BaseTreeDaoImpl<T extends BaseEntity & TreeEntity> extends BaseDaoImpl<T, String> implements BaseTreeDao<T> {
//    private static final Logger logger = LoggerFactory.getLogger(BaseTreeDaoImpl.class);

    public BaseTreeDaoImpl(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
    }

    ////////////////////////////////////

    /**
     * 获取所有树根节点
     *
     * @return 返回树根节点集合
     */
    @Override
    @Transactional(readOnly = true)
    public List<T> getAllRootNode() {
        Search search = new Search();
        search.addFilter(new SearchFilter(TreeEntity.PARENT_ID, SearchFilter.EMPTY_VALUE, SearchFilter.Operator.BK));
        search.addSortOrder(SearchOrder.asc(TreeEntity.RANK));
        List<T> list = findByFilters(search);
        return list;
    }

    /**
     * 获取指定节点下的所有子节点(包含自己)
     *
     * @param nodeId 当前节点ID
     * @return 返回指定节点下的所有子节点(包含自己)
     */
    @Override
    @Transactional(readOnly = true)
    public List<T> getChildrenNodes(String nodeId) {
        Assert.notNull(nodeId, "nodeId不能为空");

        List<T> nodeList = new ArrayList<T>();
        //获取当前节点
        T entity = findOne(nodeId);
        if (Objects.nonNull(entity)) {
            nodeList.addAll(findByCodePathStartingWith(entity.getCodePath()));
        }
        return nodeList;
    }

    /**
     * 获取指定节点下的所有子节点(不包含自己)
     *
     * @param nodeId 当前节点ID
     * @return 返回指定节点下的所有子节点(不包含自己)
     */
    @Override
    @Transactional(readOnly = true)
    public List<T> getChildrenNodesNoneOwn(String nodeId) {
        Assert.notNull(nodeId, "nodeId不能为空");

        List<T> nodeList = new ArrayList<T>();
        //获取当前节点
        T entity = findOne(nodeId);
        if (Objects.nonNull(entity)) {
            nodeList.addAll(findByCodePathStartingWithAndIdNot(entity.getCodePath(), nodeId));
        }
        return nodeList;
    }

    /**
     * 获取指定节点名称的所有节点
     *
     * @param nodeName 当前节点名称
     * @return 返回指定节点名称的所有节点
     */
    @Override
    @Transactional(readOnly = true)
    public List<T> getChildrenNodesByName(String nodeName) {
        Assert.notNull(nodeName, "nodeName不能为空");

        List<T> nodeList = findByNamePathLike(nodeName);
        if (CollectionUtils.isEmpty(nodeList)) {
            nodeList = new ArrayList<T>();
        }
        return nodeList;
    }

    /**
     * 获取树
     *
     * @param nodeId 当前节点ID
     * @return 返回指定节点树形对象
     */
    @Override
    @Transactional(readOnly = true)
    public T getTree(String nodeId) {
        Assert.notNull(nodeId, "nodeId不能为空");

        T tree = findOne(nodeId);
        if (Objects.nonNull(tree)) {
            List<T> childrenList = getChildrenNodes(nodeId);
            if (CollectionUtils.isNotEmpty(childrenList)) {
                recursiveBuild(tree, childrenList);
            }
        }
        return tree;
    }

    /**
     * 通过代码路径获取指定路径开头的集合
     *
     * @param codePath 代码路径
     * @return 返回指定代码路径开头的集合
     */
    @Override
    public List<T> findByCodePathStartingWith(String codePath) {
        Search search = new Search();
        search.addFilter(new SearchFilter(TreeEntity.CODE_PATH, codePath, SearchFilter.Operator.LLK));
        search.addSortOrder(SearchOrder.asc(TreeEntity.RANK));
        List<T> list = findByFilters(search);
        return list;
    }

    /**
     * 获取指定节点下的所有子节点(不包含自己)
     *
     * @param codePath
     * @param nodeId
     * @return
     */
    @Override
    public List<T> findByCodePathStartingWithAndIdNot(String codePath, String nodeId) {
        Search search = new Search();
        search.addFilter(new SearchFilter(TreeEntity.CODE_PATH, codePath, SearchFilter.Operator.LLK));
        search.addFilter(new SearchFilter(BaseEntity.ID, nodeId, SearchFilter.Operator.NE));
        search.addSortOrder(SearchOrder.asc(TreeEntity.RANK));
        List<T> list = findByFilters(search);
        return list;
    }

    @Override
    public List<T> findByNamePathStartingWith(String namePath) {
        Search search = new Search();
        search.addFilter(new SearchFilter(TreeEntity.NAME_PATH, namePath, SearchFilter.Operator.LLK));
        search.addSortOrder(SearchOrder.asc(TreeEntity.RANK));
        List<T> list = findByFilters(search);
        return list;
    }

    /**
     * @param namePath
     * @param nodeId
     * @return
     */
    @Override
    public List<T> findByNamePathStartingWithAndIdNot(String namePath, String nodeId) {
        Search search = new Search();
        search.addFilter(new SearchFilter(TreeEntity.NAME_PATH, namePath, SearchFilter.Operator.LLK));
        search.addFilter(new SearchFilter(BaseEntity.ID, nodeId, SearchFilter.Operator.NE));
        search.addSortOrder(SearchOrder.asc(TreeEntity.RANK));
        List<T> list = findByFilters(search);
        return list;
    }

    /**
     * 节点名称模糊获取节点
     *
     * @param nodeName 节点名称
     * @return 返回含有指定节点名称的集合
     */
    @Override
    public List<T> findByNamePathLike(String nodeName) {
        Search search = new Search();
        search.addFilter(new SearchFilter(TreeEntity.NAME_PATH, nodeName, SearchFilter.Operator.LK));
        search.addSortOrder(SearchOrder.asc(TreeEntity.RANK));
        List<T> list = findByFilters(search);
        return list;
    }

    /**
     * 递归构造树
     */
    private T recursiveBuild(T parentNode, List<T> nodes) {
        List<T> children = parentNode.getChildren();
        if (Objects.isNull(children)) {
            children = new ArrayList<T>();
        }

        for (T treeNode : nodes) {
            if (Objects.equals(parentNode.getId(), treeNode.getParentId())) {
                T treeEntity = recursiveBuild(treeNode, nodes);

                children.add(treeEntity);
            }
        }
        parentNode.setChildren(children);
        return parentNode;
    }

    /////////////////////////////以下为冻结特性的方法/////////////////////////

    /**
     * @param id
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public T findOne4Unfrozen(String id) {
        Assert.notNull(id, "主键不能为空");
        if (IFrozen.class.isAssignableFrom(domainClass)) {
            Specification<T> spec = (root, query, builder) -> {
                if (ITenant.class.isAssignableFrom(domainClass)) {
                    Predicate tenantCodePredicate = builder.equal(root.get(ITenant.TENANT_CODE), ContextUtil.getTenantCode());
                    Predicate idPredicate = builder.equal(root.get(BaseEntity.ID), id);
                    Predicate frozenPredicate = builder.equal(root.get(IFrozen.FROZEN), Boolean.FALSE);

                    Predicate[] predicates = new Predicate[]{frozenPredicate, tenantCodePredicate, idPredicate};
                    return builder.and(predicates);
                } else {
                    return builder.and(builder.equal(root.get(IFrozen.FROZEN), Boolean.FALSE), builder.equal(root.get(BaseEntity.ID), id));
                }
            };
            return findOne(spec).orElse(null);
        } else {
            return findOne(id);
        }
    }

    /**
     * 获取所有未冻结的树根节点
     *
     * @return 返回树根节点集合
     */
    @Transactional(readOnly = true)
    public List<T> getAllRootNode4Unfrozen() {
        List<T> list;
        if (IFrozen.class.isAssignableFrom(domainClass)) {
            Search search = new Search();
            search.addFilter(new SearchFilter(TreeEntity.PARENT_ID, SearchFilter.EMPTY_VALUE, SearchFilter.Operator.BK));
            search.addFilter(new SearchFilter(IFrozen.FROZEN, Boolean.FALSE, SearchFilter.Operator.EQ));
            search.addSortOrder(SearchOrder.asc(IRank.RANK));
            list = findByFilters(search);
        } else {
            list = getAllRootNode();
        }
        return list;
    }

    /**
     * 获取指定节点下的所有子节点(包含自己)
     *
     * @param nodeId 当前节点ID
     * @return 返回指定节点下的所有子节点(包含自己)
     */
    @Override
    @Transactional(readOnly = true)
    public List<T> getChildrenNodes4Unfrozen(String nodeId) {
        Assert.notNull(nodeId, "nodeId不能为空");

        List<T> nodeList = new ArrayList<T>();
        //获取当前节点
        T entity = this.findOne4Unfrozen(nodeId);
        if (Objects.nonNull(entity)) {
            nodeList.addAll(findByCodePathStartWith4Unfrozen(entity.getCodePath()));
        }
        return nodeList;
    }

    /**
     * 获取指定节点下的所有子节点(不包含自己)
     *
     * @param nodeId 当前节点ID
     * @return 返回指定节点下的所有子节点(不包含自己)
     */
    @Override
    @Transactional(readOnly = true)
    public List<T> getChildrenNodesNoneOwn4Unfrozen(String nodeId) {
        Assert.notNull(nodeId, "nodeId不能为空");

        List<T> nodeList = new ArrayList<T>();
        //获取当前节点
        T entity = this.findOne4Unfrozen(nodeId);
        if (Objects.nonNull(entity)) {
            nodeList.addAll(findByCodePathStartWithAndIdNot4Unfrozen(entity.getCodePath(), nodeId));
        }
        return nodeList;
    }

    /**
     * 获取指定节点名称的所有节点
     *
     * @param nodeName 当前节点名称
     * @return 返回指定节点名称的所有节点
     */
    @Override
    @Transactional(readOnly = true)
    public List<T> getChildrenNodesByName4Unfrozen(String nodeName) {
        Assert.notNull(nodeName, "nodeName不能为空");

        List<T> nodeList = findByNamePathLike4Unfrozen(nodeName);
        if (CollectionUtils.isEmpty(nodeList)) {
            nodeList = new ArrayList<T>();
        }
        return nodeList;
    }

    /**
     * 获取树
     *
     * @param nodeId 当前节点ID
     * @return 返回指定节点树形对象
     */
    @Override
    @Transactional(readOnly = true)
    public T getTree4Unfrozen(String nodeId) {
        Assert.notNull(nodeId, "nodeId不能为空");

        T tree = this.findOne4Unfrozen(nodeId);
        if (Objects.nonNull(tree)) {
            List<T> childrenList = getChildrenNodes4Unfrozen(nodeId);
            if (CollectionUtils.isNotEmpty(childrenList)) {
                recursiveBuild4Unfrozen(tree, childrenList);
            }
        }
        return tree;
    }

    /**
     * 通过代码路径获取指定路径开头的集合
     *
     * @param codePath 代码路径
     * @return 返回指定代码路径开头的集合
     */
    @Override
    public List<T> findByCodePathStartWith4Unfrozen(String codePath) {
        List<T> list;
        if (IFrozen.class.isAssignableFrom(domainClass)) {
            Search search = new Search();
            search.addFilter(new SearchFilter(TreeEntity.CODE_PATH, codePath, SearchFilter.Operator.LLK));
            search.addFilter(new SearchFilter(IFrozen.FROZEN, Boolean.FALSE, SearchFilter.Operator.EQ));
            search.addSortOrder(SearchOrder.asc(TreeEntity.RANK));
            list = findByFilters(search);
        } else {
            list = findByCodePathStartingWith(codePath);
        }
        return list;
    }

    /**
     * 获取指定节点下的所有子节点(不包含自己)
     *
     * @param codePath
     * @param nodeId
     * @return
     */
    @Override
    public List<T> findByCodePathStartWithAndIdNot4Unfrozen(String codePath, String nodeId) {
        List<T> list;
        if (IFrozen.class.isAssignableFrom(domainClass)) {
            Search search = new Search();
            search.addFilter(new SearchFilter(TreeEntity.CODE_PATH, codePath, SearchFilter.Operator.LLK));
            search.addFilter(new SearchFilter(BaseEntity.ID, nodeId, SearchFilter.Operator.NE));
            search.addFilter(new SearchFilter(IFrozen.FROZEN, Boolean.FALSE, SearchFilter.Operator.EQ));
            search.addSortOrder(SearchOrder.asc(TreeEntity.RANK));
            list = findByFilters(search);
        } else {
            list = findByCodePathStartingWithAndIdNot(codePath, nodeId);
        }
        return list;
    }

    @Override
    public List<T> findByNamePathStartWith4Unfrozen(String namePath) {
        List<T> list;
        if (IFrozen.class.isAssignableFrom(domainClass)) {
            Search search = new Search();
            search.addFilter(new SearchFilter(TreeEntity.NAME_PATH, namePath, SearchFilter.Operator.LLK));
            search.addFilter(new SearchFilter(IFrozen.FROZEN, Boolean.FALSE, SearchFilter.Operator.EQ));
            search.addSortOrder(SearchOrder.asc(TreeEntity.RANK));
            list = findByFilters(search);
        } else {
            list = findByNamePathStartingWith(namePath);
        }
        return list;
    }

    /**
     * @param namePath
     * @param nodeId
     * @return
     */
    @Override
    public List<T> findByNamePathStartWithAndIdNot4Unfrozen(String namePath, String nodeId) {
        List<T> list;
        if (IFrozen.class.isAssignableFrom(domainClass)) {
            Search search = new Search();
            search.addFilter(new SearchFilter(TreeEntity.NAME_PATH, namePath, SearchFilter.Operator.LLK));
            search.addFilter(new SearchFilter(BaseEntity.ID, nodeId, SearchFilter.Operator.NE));
            search.addFilter(new SearchFilter(IFrozen.FROZEN, Boolean.FALSE, SearchFilter.Operator.EQ));
            search.addSortOrder(SearchOrder.asc(TreeEntity.RANK));
            list = findByFilters(search);
        } else {
            list = findByNamePathStartingWithAndIdNot(namePath, nodeId);
        }
        return list;
    }

    /**
     * 节点名称模糊获取节点
     *
     * @param nodeName 节点名称
     * @return 返回含有指定节点名称的集合
     */
    @Override
    public List<T> findByNamePathLike4Unfrozen(String nodeName) {
        List<T> list;
        if (IFrozen.class.isAssignableFrom(domainClass)) {
            Search search = new Search();
            search.addFilter(new SearchFilter(TreeEntity.NAME_PATH, nodeName, SearchFilter.Operator.LK));
            search.addFilter(new SearchFilter(IFrozen.FROZEN, Boolean.FALSE, SearchFilter.Operator.EQ));
            search.addSortOrder(SearchOrder.asc(TreeEntity.RANK));
            list = findByFilters(search);
        } else {
            list = findByNamePathLike(nodeName);
        }
        return list;
    }

    /**
     * todo 递归构造树
     */
    private T recursiveBuild4Unfrozen(T parentNode, List<T> nodes) {
        List<T> children = parentNode.getChildren();
        if (Objects.isNull(children)) {
            children = new ArrayList<T>();
        }

        for (T treeNode : nodes) {
            if (Objects.equals(parentNode.getId(), treeNode.getParentId())) {
                if (treeNode instanceof IFrozen) {
                    IFrozen frozen = (IFrozen) treeNode;
                    //冻结
                    if (frozen.getFrozen()) {
                        continue;
                    }
                }

                T treeEntity = recursiveBuild4Unfrozen(treeNode, nodes);

                children.add(treeEntity);
            }
        }
        parentNode.setChildren(children);
        return parentNode;
    }
}
