package com.ecmp.core.dao.jpa.impl;

import com.ecmp.context.ContextUtil;
import com.ecmp.core.dao.jpa.BaseDao;
import com.ecmp.core.entity.*;
import com.ecmp.core.search.*;
import com.ecmp.exception.DataOperationDeniedException;
import com.ecmp.util.IdGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.QueryHint;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * <strong>实现功能:</strong>
 * <p></p>
 *
 * @param <T>  Persistable的子类
 * @param <ID> Serializable的子类
 * @author <a href="mailto:chao2.ma@changhong.com">马超(Vision.Mac)</a>
 * @version 1.0.1 2017/5/8 15:33
 */
//@Repository
@SuppressWarnings("unchecked")
@Transactional(readOnly = true)
public class BaseDaoImpl<T extends Persistable & Serializable, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements BaseDao<T, ID> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseDaoImpl.class);

    protected final Class<T> domainClass;
    protected final EntityManager entityManager;

    public BaseDaoImpl(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
        this.domainClass = domainClass;
        this.entityManager = entityManager;
    }

    /**
     * 持久化实体对象
     *
     * @param entity
     * @param <S>
     * @return
     */
    @Override
    @Transactional
    public <S extends T> S save(S entity) {
        boolean isNew = preSave(entity);
        if (isNew) {
            entityManager.persist(entity);
        } else {
            entity = entityManager.merge(entity);
        }
        return entity;
    }

    @Override
    @Transactional
    public void save(Collection<T> entities) {
        if (entities != null && entities.size() > 0) {
            int i = 0;
            for (T entity : entities) {
                i++;
                boolean isNew = preSave(entity);
                if (isNew) {
                    entityManager.persist(entity);
                } else {
                    entityManager.merge(entity);
                }
                if (i % 50 == 0) {
                    entityManager.flush();
                    entityManager.clear();
                }
            }
        }
    }

    private boolean preSave(T entity) {
        Assert.notNull(entity, "持久化实体对象不能为空。");

        boolean isNew;
        if (entity instanceof BaseEntity) {
            BaseEntity baseEntity = (BaseEntity) entity;
            ID id = (ID) baseEntity.getId();
            //id是否有值
            if (Objects.isNull(id)) {
                isNew = true;
                baseEntity.setId(IdGenerator.uuid());
            } else {
//                T origin = findOne(id);
//                isNew = Objects.isNull(origin);
                if (!existsById(id)) {
                    throw new DataOperationDeniedException(ContextUtil.getMessage("ecmp_service_00004", id));
                } else {
                    isNew = false;
                }
            }
        } else {
            isNew = Objects.isNull(entity.getId());
        }
        //是否含有业务审计属性实体
        if (entity instanceof IAuditable) {
            Date now = new Date();
            String userId = ContextUtil.getUserId();
            String userAccount = ContextUtil.getUserAccount();
            String userName = ContextUtil.getUserName();
            IAuditable auditableEntity = (IAuditable) entity;
            if (isNew) {
                //创建
                auditableEntity.setCreatorId(userId);
                auditableEntity.setCreatorName(userName);
                auditableEntity.setCreatorAccount(userAccount);
                auditableEntity.setCreatedDate(now);
            }
            auditableEntity.setLastEditorId(userId);
            auditableEntity.setLastEditorName(userName);
            auditableEntity.setLastEditorAccount(userAccount);
            auditableEntity.setLastEditedDate(now);
        }
        //是否是租户实体(只是在租户代码为空时设置)
        if (entity instanceof ITenant && StringUtils.isBlank(((ITenant) entity).getTenantCode())) {
            //从上下文中获取租户代码
            ITenant tenant = (ITenant) entity;
            tenant.setTenantCode(ContextUtil.getTenantCode());
        }
        return isNew;
    }

    /**
     * 基于主键集合查询集合数据对象
     */
    @Override
    @Transactional(readOnly = true)
    public List<T> findAll() {
        Sort sort = Sort.unsorted();
        if (IRank.class.isAssignableFrom(domainClass)) {
            sort = Sort.by(Sort.Direction.ASC, IRank.RANK);
        }

        if (ITenant.class.isAssignableFrom(domainClass)) {
            Specification<T> spec = (root, query, builder) -> {
                Path expression = root.get(ITenant.TENANT_CODE);
                return builder.equal(expression, ContextUtil.getTenantCode());
            };
            return super.findAll(spec, sort);
        } else {
            return super.findAll(sort);
        }
    }

    /**
     * 基于主键查询单一数据对象
     */
    @Override
    @Transactional(readOnly = true)
    public T findOne(ID id) {
        return findById(id).orElse(null);
    }

    /**
     * Retrieves an entity by its id.
     *
     * @param id must not be {@literal null}.
     * @return the entity with the given id or {@literal Optional#empty()} if none found
     * @throws IllegalArgumentException if {@code id} is {@literal null}.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<T> findById(ID id) {
        T entity = null;
        if (Objects.nonNull(id)) {
            //Assert.notNull(id, "主键不能为空");
            //通过findOne(id)走二级缓存查询
            /*Specification<T> spec = (root, query, builder) -> {
                if (ITenant.class.isAssignableFrom(domainClass)) {
                    Predicate tenantCodePredicate = builder.equal(root.get(ITenant.TENANT_CODE), ContextUtil.getTenantCode());
                    Predicate idPredicate = builder.equal(root.get(BaseEntity.ID), id);
                    return builder.and(tenantCodePredicate, idPredicate);
                } else {
                    return builder.equal(root.get(BaseEntity.ID), id);
                }
            };
            return findOne(spec);*/
            entity = super.findById(id).orElse(null);
            if (Objects.nonNull(entity) && ITenant.class.isAssignableFrom(domainClass)) {
                ITenant tenantEntity = (ITenant) entity;
                if (!StringUtils.equals(ContextUtil.getTenantCode(), tenantEntity.getTenantCode())) {
                    return Optional.empty();
                }
            }
        }
        return Optional.ofNullable(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void evict() {
        entityManager.getEntityManagerFactory().getCache().evict(domainClass);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void evict(ID id) {
        if (Objects.nonNull(id)) {
            entityManager.getEntityManagerFactory().getCache().evict(domainClass, id);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void evictAll() {
        entityManager.getEntityManagerFactory().getCache().evictAll();
    }

    /////////////////////////////////自定义方法/////////////////////////////////////

    /**
     * 获取业务实体类型
     *
     * @return 业务实体类型
     */
    @Override
    public Class<T> getEntityClass() {
        return domainClass;
    }

    /**
     * 通过Id清单删除业务实体
     *
     * @param id 业务实体id清单
     */
    @Override
    @Transactional
    public void delete(ID id) {
        deleteById(id);
    }

    /**
     * 通过Id清单删除业务实体
     *
     * @param ids 业务实体id清单
     */
    @Override
    @Transactional
    public void delete(Collection<ID> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        for (ID id : ids) {
            deleteById(id);
        }
    }

    /**
     * 获取所有未冻结的
     *
     * @return 返回未冻结的数据集合
     */
    @Override
    @Transactional(readOnly = true)
    public List<T> findAllUnfrozen() {
        Sort sort = Sort.unsorted();
        if (IRank.class.isAssignableFrom(domainClass)) {
            sort = Sort.by(Sort.Direction.ASC, IRank.RANK);
        }

        Specification<T> spec = (root, query, builder) -> {
            Predicate predicate = null;
            //冻结
            Predicate frozenPredicate = null;
            if (IFrozen.class.isAssignableFrom(domainClass)) {
                frozenPredicate = builder.equal(root.get(IFrozen.FROZEN), false);
            }
            //租户
            Predicate tenantCodePredicate = null;
            if (ITenant.class.isAssignableFrom(domainClass)) {
                Path expression = root.get(ITenant.TENANT_CODE);
                tenantCodePredicate = builder.equal(expression, ContextUtil.getTenantCode());
            }

            if (frozenPredicate != null && tenantCodePredicate != null) {
                predicate = builder.and(frozenPredicate, tenantCodePredicate);
            } else if (frozenPredicate != null) {
                predicate = frozenPredicate;
            } else if (tenantCodePredicate != null) {
                predicate = tenantCodePredicate;
            }
            return predicate;
        };
        return super.findAll(spec, sort);
    }

    /**
     * 根据泛型对象属性和值查询集合对象
     *
     * @param property 属性名，即对象中数量变量名称
     * @param value    参数值
     */
    @Override
    @Transactional(readOnly = true)
    public List<T> findListByProperty(final String property, final Object value) {
        /*Specification<T> spec = (root, query, builder) -> {
            if (ITenant.class.isAssignableFrom(domainClass)) {
                Predicate tenantCodePredicate = builder.equal(root.get(ITenant.TENANT_CODE), ContextUtil.getTenantCode());
                Predicate propertyPredicate = builder.equal(root.get(property), value);
                return builder.and(propertyPredicate, tenantCodePredicate);
            } else {
                Path expression = root.get(property);
                return builder.equal(expression, value);
            }
        };
        return findAll(spec);*/
        Specification<T> spec = (root, query, builder) -> {
            if (ITenant.class.isAssignableFrom(domainClass)) {
                SearchFilter searchFilter = new SearchFilter(property, value, SearchFilter.Operator.EQ, null);
                Predicate propertyPredicate = buildPredicate(property, searchFilter, root, query, builder, false);
                Predicate tenantCodePredicate = builder.equal(root.get(ITenant.TENANT_CODE), ContextUtil.getTenantCode());
                return builder.and(propertyPredicate, tenantCodePredicate);
            } else {
                SearchFilter searchFilter = new SearchFilter(property, value, SearchFilter.Operator.EQ, null);
                return buildPredicate(property, searchFilter, root, query, builder, false);
            }
        };
        return findAll(spec);
    }

    /**
     * 根据泛型对象属性和值查询唯一对象
     *
     * @param property 属性名，即对象中数量变量名称
     * @param value    参数值
     * @return 未查询到返回null，如果查询到多条数据则抛出异常
     */
    @Override
    @Transactional(readOnly = true)
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    public T findByProperty(final String property, final Object value) {
        List<T> entities = findListByProperty(property, value);
        if (CollectionUtils.isEmpty(entities)) {
            return null;
        } else {
            Assert.isTrue(entities.size() == 1, "查询到多条数据");
            return entities.get(0);
        }
    }

    /**
     * 根据泛型对象属性和值查询唯一对象(无序)
     *
     * @param property 属性名，即对象中数量变量名称
     * @param value    参数值
     * @return 未查询到返回null，如果查询到多条数据则返回第一条
     */
    @Override
    @Transactional(readOnly = true)
    public T findFirstByProperty(final String property, final Object value) {
        /*List<T> entities = findListByProperty(property, value);
        if (CollectionUtils.isEmpty(entities)) {
            return null;
        } else {
            return entities.get(0);
        }*/
        Pageable pageable = PageRequest.of(0, 1);
        /*Specification<T> spec = (root, query, builder) -> {
            if (ITenant.class.isAssignableFrom(domainClass)) {
                Predicate tenantCodePredicate = builder.equal(root.get(ITenant.TENANT_CODE), ContextUtil.getTenantCode());
                Expression expression = buildExpression(root, builder, property, null);
                Predicate propertyPredicate = builder.equal(expression, value);
                return builder.and(propertyPredicate, tenantCodePredicate);
            } else {
                Expression expression = buildExpression(root, builder, property, null);
                return builder.equal(expression, value);
            }
        };*/
        Specification<T> spec = (root, query, builder) -> {
            if (ITenant.class.isAssignableFrom(domainClass)) {
                SearchFilter searchFilter = new SearchFilter(property, value, SearchFilter.Operator.EQ, null);
                Predicate propertyPredicate = buildPredicate(property, searchFilter, root, query, builder, false);
                Predicate tenantCodePredicate = builder.equal(root.get(ITenant.TENANT_CODE), ContextUtil.getTenantCode());
                return builder.and(propertyPredicate, tenantCodePredicate);
            } else {
                SearchFilter searchFilter = new SearchFilter(property, value, SearchFilter.Operator.EQ, null);
                return buildPredicate(property, searchFilter, root, query, builder, false);
            }
        };
        Page<T> page = findAll(spec, pageable);
        T result = null;
        if (Objects.nonNull(page)) {
            List<T> list = page.getContent();
            if (CollectionUtils.isNotEmpty(list)) {
                result = list.get(0);
            }
        }
        return result;
    }

    /**
     * 根据业务对象属性的值判断业务实体是否存在
     *
     * @param property 属性名，即对象中数量变量名称
     * @param value    参数值
     * @return 未查询到返回false，如果查询到一条或多条数据则返回true
     */
    @Override
    @Transactional(readOnly = true)
    public boolean isExistsByProperty(String property, Object value) {
        return Objects.nonNull(findFirstByProperty(property, value));
    }

    /**
     * 单一条件对象查询数据集合
     */
    @Override
    @Transactional(readOnly = true)
    public List<T> findByFilter(SearchFilter searchFilter) {
        List<SearchFilter> searchFilters = new ArrayList<SearchFilter>();
        searchFilters.add(searchFilter);
        Search searchConfig = new Search(searchFilters, null, null);
        Specification<T> spec = buildSpecification(searchConfig);
        return findAll(spec);
    }

    /**
     * 基于查询条件count记录数据
     */
    @Override
    @Transactional(readOnly = true)
    public long count(Search searchConfig) {
        Specification<T> spec = buildSpecification(searchConfig);
        return count(spec);
    }

    /**
     * 基于动态组合条件对象查询数据
     */
    @Override
    @Transactional(readOnly = true)
    public T findOneByFilters(Search searchConfig) {
        Specification<T> spec = buildSpecification(searchConfig);
        Optional<T> optional = findOne(spec);
        return optional.orElse(null);
    }

    /**
     * 基于动态组合条件对象和排序定义查询数据集合
     */
    @Override
    @Transactional(readOnly = true)
    public List<T> findByFilters(Search searchConfig) {
        Sort sort = buildSort(searchConfig);
        Specification<T> spec = buildSpecification(searchConfig);
        return findAll(spec, sort);
    }

    /**
     * 基于动态组合条件对象和分页(含排序)对象查询数据集合
     */
    @Override
    @Transactional(readOnly = true)
    public PageResult<T> findByPage(Search searchConfig) {
        PageInfo pageInfo = searchConfig.getPageInfo();
        //Assert.isTrue(pageInfo != null, "无分页参数。");
        if (Objects.isNull(pageInfo)) {
            pageInfo = new PageInfo();
            LOGGER.warn("无分页参数，将使用默认[{}]。", pageInfo.toString());
        }

        Sort sort = buildSort(searchConfig);
        Pageable pageable = PageRequest.of(pageInfo.getPage() - 1, pageInfo.getRows(), sort);

        Specification<T> specifications = buildSpecification(searchConfig);

        Page<T> page = findAll(specifications, pageable);
        PageResult<T> pageResult = new PageResult<T>();
        pageResult.setPage(page.getNumber() + 1);
        pageResult.setRecords(page.getTotalElements());
        pageResult.setTotal(page.getTotalPages());
        pageResult.setRows(page.getContent());
        return pageResult;
    }

    /**
     * 检查代码是否已经存在
     *
     * @param code 代码
     * @param id   Id标识
     * @return 是否已经存在
     */
    @Override
    @Transactional(readOnly = true)
    public boolean isCodeExists(String code, String id) {
        if (ICodeUnique.class.isAssignableFrom(domainClass)) {
            String queryStr = String.format("select t.id from %s t where t.code=:code and t.id<>:id", domainClass.getSimpleName());
            Query query = entityManager.createQuery(queryStr);
            query.setParameter(ICodeUnique.CODE_FIELD, code);
            query.setParameter(BaseEntity.ID, id);
            query.setMaxResults(1);
            List result = query.getResultList();
            return !result.isEmpty();
        }
        return false;
    }

    /**
     * 检查租户中代码是否已经存在
     *
     * @param tenantCode 租户代码
     * @param code       代码
     * @param id         Id标识
     * @return 是否已经存在
     */
    @Override
    @Transactional(readOnly = true)
    public boolean isCodeExists(String tenantCode, String code, String id) {
        if (ICodeUnique.class.isAssignableFrom(domainClass) && ITenant.class.isAssignableFrom(domainClass)) {
            String queryStr =
                    String.format("select t.id from %s t where t.tenantCode=:tenantCode and t.code=:code and t.id<>:id", domainClass.getSimpleName());
            Query query = entityManager.createQuery(queryStr);
            query.setParameter(ITenant.TENANT_CODE, tenantCode);
            query.setParameter(ICodeUnique.CODE_FIELD, code);
            query.setParameter(BaseEntity.ID, id);
            query.setMaxResults(1);
            List result = query.getResultList();
            return !result.isEmpty();
        }
        return false;
    }

    private Sort buildSort(Search searchConfig) {
        Sort sort = Sort.unsorted();
        if (searchConfig != null) {
            List<SearchOrder> sortOrders = searchConfig.getSortOrders();
            if (sortOrders != null) {
                List<Sort.Order> orders = new LinkedList<Sort.Order>();
                for (SearchOrder sortOrder : sortOrders) {
                    if (SearchOrder.Direction.DESC.equals(sortOrder.getDirection())) {
                        orders.add(new Sort.Order(Sort.Direction.DESC, sortOrder.getProperty()));
                    } else {
                        orders.add(new Sort.Order(Sort.Direction.ASC, sortOrder.getProperty()));
                    }
                }
                sort = Sort.by(orders);
            }
        }
        return sort;
    }

    private <X> Predicate buildPredicate(String propertyName, SearchFilter filter, Root<X> root, CriteriaQuery<?> query,
                                         CriteriaBuilder builder, Boolean having) {
        Object matchValue = filter.getValue();
        if ((matchValue == null)
                || (having && !propertyName.contains("("))
                || (!having && propertyName.contains("("))) {
            return null;
        }
        if (matchValue instanceof String) {
            if (StringUtils.isBlank(String.valueOf(matchValue))) {
                return null;
            }
        } else if (matchValue.getClass().isArray()) {
            if (((Object[]) matchValue).length == 0) {
                return null;
            }
        } else if (matchValue instanceof Collection) {
            if (((Collection) matchValue).size() == 0) {
                return null;
            }
        }

        Predicate predicate = null;
        Expression expression = buildExpression(root, builder, propertyName, null);
        if (SearchFilter.NULL_VALUE.equalsIgnoreCase(String.valueOf(matchValue))) {
            predicate = expression.isNull();
        } else if (SearchFilter.EMPTY_VALUE.equalsIgnoreCase(String.valueOf(matchValue))) {
            predicate = builder.or(builder.isNull(expression), builder.equal(expression, ""));
        } else if (SearchFilter.NO_NULL_VALUE.equalsIgnoreCase(String.valueOf(matchValue))) {
            predicate = expression.isNotNull();
        } else if (SearchFilter.NO_EMPTY_VALUE.equalsIgnoreCase(String.valueOf(matchValue))) {
            predicate = builder.and(builder.isNotNull(expression), builder.notEqual(expression, ""));
        } else {
            // logic operator
            switch (filter.getOperator()) {
                case EQ:
                    // 对日期特殊处理：一般用于区间日期的结束时间查询,如查询2012-01-01之前,一般需要显示2010-01-01当天及以前的数据,
                    // 而数据库一般存有时分秒,因此需要特殊处理把当前日期+1天,转换为<2012-01-02进行查询
                    if (matchValue instanceof Date) {
                        DateTime dateTime = new DateTime(((Date) matchValue).getTime());
                        if (dateTime.getHourOfDay() == 0 && dateTime.getMinuteOfHour() == 0 && dateTime.getSecondOfMinute() == 0) {
                            predicate = builder.and(builder.greaterThanOrEqualTo(expression, dateTime.toDate()),
                                    builder.lessThan(expression, dateTime.plusDays(1).toDate()));
                        }
                    }
                    if (predicate == null) {
                        predicate = builder.equal(expression, matchValue);
                    }
                    break;
                case NE:
                    // 对日期特殊处理：一般用于区间日期的结束时间查询,如查询2012-01-01之前,一般需要显示2010-01-01当天及以前的数据,
                    // 而数据库一般存有时分秒,因此需要特殊处理把当前日期+1天,转换为<2012-01-02进行查询
                    if (matchValue instanceof Date) {
                        DateTime dateTime = new DateTime(((Date) matchValue).getTime());
                        if (dateTime.getHourOfDay() == 0 && dateTime.getMinuteOfHour() == 0 && dateTime.getSecondOfMinute() == 0) {
                            predicate = builder.or(builder.lessThan(expression, dateTime.toDate()),
                                    builder.greaterThan(expression, dateTime.plusDays(1).toDate()));
                        }
                    }
                    if (predicate == null) {
                        predicate = builder.notEqual(expression, matchValue);
                    }
                    break;
                case BK://IS NULL OR ==''
                    predicate = builder.or(builder.isNull(expression), builder.equal(expression, ""));
                    break;
                case NB://IS NOT NULL AND !=''
                    predicate = builder.and(builder.isNotNull(expression), builder.notEqual(expression, ""));
                    break;
                case NU://IS NULL
                    if (matchValue instanceof Boolean && !((Boolean) matchValue)) {
                        predicate = builder.isNotNull(expression);
                    } else {
                        predicate = builder.isNull(expression);
                    }
                    break;
                case NN://IS NOT NULL
                    if (matchValue instanceof Boolean && !((Boolean) matchValue)) {
                        predicate = builder.isNull(expression);
                    } else {
                        predicate = builder.isNotNull(expression);
                    }
                    break;
                case LK://LIKE %abc%
                    predicate = builder.like(expression, "%" + matchValue + "%");
                    break;
                case NC://NOT LIKE %abc%
                    predicate = builder.notLike(expression, "%" + matchValue + "%");
                    break;
                case LLK://LIKE abc%
                    predicate = builder.like(expression, matchValue + "%");
                    break;
                case BN://NOT LIKE abc%
                    predicate = builder.notLike(expression, matchValue + "%");
                    break;
                case RLK://LIKE %abc
                    predicate = builder.like(expression, "%" + matchValue);
                    break;
                case EN://NOT LIKE %abc
                    predicate = builder.notLike(expression, "%" + matchValue);
                    break;
                case BT://BETWEEN 1 AND 2
                    Assert.isTrue(matchValue.getClass().isArray(), "Match value must be array");
                    Object[] matchValues = (Object[]) matchValue;
                    Assert.isTrue(matchValues.length == 2, "Match value must have two value");
                    // 对日期特殊处理：一般用于区间日期的结束时间查询,如查询2012-01-01之前,一般需要显示2010-01-01当天及以前的数据,
                    // 而数据库一般存有时分秒,因此需要特殊处理把当前日期+1天,转换为<2012-01-02进行查询
                    if (matchValues[0] instanceof Date) {
                        DateTime dateFrom = new DateTime(((Date) matchValues[0]).getTime());
                        DateTime dateTo = new DateTime(((Date) matchValues[1]).getTime());
                        if (dateFrom.getHourOfDay() == 0 && dateFrom.getMinuteOfHour() == 0 && dateFrom.getSecondOfMinute() == 0) {
                            predicate = builder.and(builder.greaterThanOrEqualTo(expression, dateFrom.toDate()),
                                    builder.lessThan(expression, dateTo.plusDays(1).toDate()));
                        } else {
                            predicate = builder.equal(expression, matchValue);
                        }
                    } else {
                        predicate = builder.between(expression, (Comparable) matchValues[0], (Comparable) matchValues[1]);
                    }
                    break;
                case GT://>
                    predicate = builder.greaterThan(expression, (Comparable) matchValue);
                    break;
                case GE://>=
                    predicate = builder.greaterThanOrEqualTo(expression, (Comparable) matchValue);
                    break;
                case LT://<
                    // 对日期特殊处理：一般用于区间日期的结束时间查询,如查询2012-01-01之前,一般需要显示2010-01-01当天及以前的数据,
                    // 而数据库一般存有时分秒,因此需要特殊处理把当前日期+1天,转换为<2012-01-02进行查询
                    if (matchValue instanceof Date) {
                        DateTime dateTime = new DateTime(((Date) matchValue).getTime());
                        if (dateTime.getHourOfDay() == 0 && dateTime.getMinuteOfHour() == 0 && dateTime.getSecondOfMinute() == 0) {
                            predicate = builder.lessThan(expression, dateTime.toDate());
                        }
                    }
                    if (predicate == null && matchValue instanceof Comparable) {
                        predicate = builder.lessThan(expression, (Comparable) matchValue);
                    }
                    break;
                case LE://<=
                    // 对日期特殊处理：一般用于区间日期的结束时间查询,如查询2012-01-01之前,一般需要显示2010-01-01当天及以前的数据,
                    // 而数据库一般存有时分秒,因此需要特殊处理把当前日期+1天,转换为<2012-01-02进行查询
                    if (matchValue instanceof Date) {
                        DateTime dateTime = new DateTime(((Date) matchValue).getTime());
                        if (dateTime.getHourOfDay() == 0 && dateTime.getMinuteOfHour() == 0 && dateTime.getSecondOfMinute() == 0) {
                            predicate = builder.lessThan(expression, dateTime.plusDays(1).toDate());
                        }
                    }
                    if (predicate == null && matchValue instanceof Comparable) {
                        predicate = builder.lessThanOrEqualTo(expression, (Comparable) matchValue);
                    }
                    break;
                case IN:
                    if (matchValue.getClass().isArray()) {
                        predicate = expression.in((Object[]) matchValue);
                    } else if (matchValue instanceof Collection) {
                        predicate = expression.in((Collection) matchValue);
                    } else {
                        predicate = builder.equal(expression, matchValue);
                    }
                    break;
                case PLT://Property Less Equal: <
                    Expression expressionPLT = buildExpression(root, builder, (String) matchValue, null);
                    predicate = builder.lessThan(expression, expressionPLT);
                    break;
                case PLE://Property Less Than: <=
                    Expression expressionPLE = buildExpression(root, builder, (String) matchValue, null);
                    predicate = builder.lessThanOrEqualTo(expression, expressionPLE);
                    break;
                default:
                    break;
            }
        }

        Assert.notNull(predicate, "Undefined match type: " + filter.getOperator());
        return predicate;
    }

    /**
     * 根据条件集合对象组装JPA规范条件查询集合对象，基类默认实现进行条件封装组合
     * 子类可以调用此方法在返回的List<Predicate>额外追加其他PropertyFilter不易表单的条件如exist条件处理等
     */
    private <X> List<Predicate> buildPredicatesFromFilters(final Collection<SearchFilter> filters, Root<X> root, CriteriaQuery<?> query,
                                                           CriteriaBuilder builder, Boolean having) {
        List<Predicate> predicates = new ArrayList<Predicate>();
        if (CollectionUtils.isNotEmpty(filters)) {
            for (SearchFilter filter : filters) {
                Predicate predicate = buildPredicate(filter.getFieldName(), filter, root, query, builder, having);
                if (predicate != null) {
                    predicates.add(predicate);
                }
            }
        }
        return predicates;
    }

    private <X extends Persistable> Specification<X> buildSpecification(final Search searchConfig) {
        return (root, query, builder) -> {
            if (searchConfig != null) {
                return buildPredicatesFromFilters(searchConfig, root, query, builder);
            } else {
                return null;
            }
        };
    }

    protected Predicate buildPredicatesFromFilters(Search searchConfig, Root root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (ITenant.class.isAssignableFrom(domainClass)) {
            SearchFilter filter = new SearchFilter(ITenant.TENANT_CODE, ContextUtil.getTenantCode(), SearchFilter.Operator.EQ);
            List<SearchFilter> filters = searchConfig.getFilters();
            if (filters == null || filters.size() == 0 || filters.stream().noneMatch(filter::equals)) {
                //追加租户代码
                searchConfig.addFilter(filter);
            }
        }
        return buildPredicatesFromFilters(searchConfig, root, query, builder, false);
    }

    private Predicate buildPredicatesFromFilters(Search searchConfig, Root root, CriteriaQuery<?> query, CriteriaBuilder builder, Boolean having) {
        if (searchConfig == null) {
            return null;
        }
        Predicate predicate = null;
        List<Predicate> predicates = new ArrayList<Predicate>();
        //快速搜索
        Collection<String> quickSearchProperties = searchConfig.getQuickSearchProperties();
        String quickSearchValue = searchConfig.getQuickSearchValue();
        if (CollectionUtils.isNotEmpty(quickSearchProperties) && StringUtils.isNotBlank(quickSearchValue)) {
            List<SearchFilter> searchFilters = new ArrayList<SearchFilter>();
            for (String property : quickSearchProperties) {
                searchFilters.add(new SearchFilter(property, quickSearchValue, SearchFilter.Operator.LK));
            }
            List<Predicate> quickSearchPredicates = buildPredicatesFromFilters(searchFilters, root, query, builder, having);

            if (CollectionUtils.isNotEmpty(quickSearchPredicates)) {
                //若有多个，存在or分组
                if (quickSearchPredicates.size() == 1) {
                    predicate = builder.and(quickSearchPredicates.get(0));
                } else {
                    //分组
                    predicate = builder.or(quickSearchPredicates.toArray(new Predicate[quickSearchPredicates.size()]));
                }
                if (predicate != null) {
                    predicates.add(predicate);
                }
            }
        }
        //追加普通查询条件（and）
        List<Predicate> andPredicates = buildPredicatesFromFilters(searchConfig.getFilters(), root, query, builder, having);
        if (CollectionUtils.isNotEmpty(andPredicates)) {
            predicates.addAll(andPredicates);
        }
        if (predicates.size() > 0) {
            predicate = builder.and(predicates.toArray(new Predicate[predicates.size()]));
        }
        return predicate;
    }

    private Expression parseExpr(Root<?> root, CriteriaBuilder criteriaBuilder, String expr, Map<String, Expression<?>> parsedExprMap) {
        if (parsedExprMap == null) {
            parsedExprMap = new HashMap<String, Expression<?>>();
        }
        Expression<?> expression = null;
        if (expr.contains("(")) {
            int left = 0;
            char[] chars = expr.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                if (chars[i] == '(') {
                    left = i;
                }
            }
            String leftStr = expr.substring(0, left);
            String op = null;
            char[] leftStrs = leftStr.toCharArray();
            for (int i = leftStrs.length - 1; i > 0; i--) {
                if (leftStrs[i] == '(' || leftStrs[i] == ')' || leftStrs[i] == ',') {
                    op = leftStr.substring(i + 1);
                    break;
                }
            }
            if (op == null) {
                op = leftStr;
            }
            String rightStr = expr.substring(left + 1);
            String arg = StringUtils.substringBefore(rightStr, ")");
            String[] args = arg.split(",");
            //LOGGER.debug("op={},arg={}", op, arg);
            if ("case".equalsIgnoreCase(op)) {
                CriteriaBuilder.Case selectCase = criteriaBuilder.selectCase();

                Expression caseWhen = parsedExprMap.get(args[0]);

                String whenResultExpr = args[1];
                Object whenResult = parsedExprMap.get(whenResultExpr);
                if (whenResult == null) {
                    CriteriaBuilder.Case<Long> whenCase = selectCase.when(caseWhen, new BigDecimal(whenResultExpr));
                    selectCase = whenCase;
                } else {
                    CriteriaBuilder.Case<Expression<?>> whenCase = selectCase.when(caseWhen, whenResult);
                    selectCase = whenCase;
                }
                String otherwiseResultExpr = args[2];
                Object otherwiseResult = parsedExprMap.get(otherwiseResultExpr);
                if (otherwiseResult == null) {
                    expression = selectCase.otherwise(new BigDecimal(otherwiseResultExpr));
                } else {
                    expression = selectCase.otherwise((Expression<?>) otherwiseResult);
                }
            } else {
                Object[] subExpressions = new Object[args.length];
                for (int i = 0; i < args.length; i++) {
                    subExpressions[i] = parsedExprMap.get(args[i]);
                    if (subExpressions[i] == null) {
                        String name = args[i];
                        try {
                            Path<?> item;
                            if (name.contains(".")) {
                                String[] props = StringUtils.split(name, ".");
                                item = root.get(props[0]);
                                for (int j = 1; j < props.length; j++) {
                                    item = item.get(props[j]);
                                }
                            } else {
                                item = root.get(name);
                            }
                            subExpressions[i] = item;
                        } catch (Exception e) {
                            subExpressions[i] = new BigDecimal(name);
                        }
                    }
                }
                try {
                    //criteriaBuilder.quot();
                    expression = (Expression) MethodUtils.invokeMethod(criteriaBuilder, op, subExpressions);
                } catch (Exception e) {
                    LOGGER.error("Error for aggregate  setting ", e);
                }
            }

            String exprPart = op + "(" + arg + ")";
            String exprPartConvert = exprPart.replace(op + "(", op + "_").replace(arg + ")", arg + "_").replace(",", "_");
            expr = expr.replace(exprPart, exprPartConvert);
            parsedExprMap.put(exprPartConvert, expression);

            if (expr.contains("(")) {
                expression = parseExpr(root, criteriaBuilder, expr, parsedExprMap);
            }
        } else {
            Path<?> item;
            if (expr.contains(".")) {
                String[] props = StringUtils.split(expr, ".");
                item = root.get(props[0]);
                for (int j = 1; j < props.length; j++) {
                    item = item.get(props[j]);
                }
            } else {
                item = root.get(expr);
            }
            expression = item;
        }
        return expression;
    }

    private Expression<?> buildExpression(Root<?> root, CriteriaBuilder criteriaBuilder, String name, String alias) {
        Expression<?> expr = parseExpr(root, criteriaBuilder, name, null);
        if (alias != null) {
            expr.alias(alias);
        }
        return expr;
    }
}
