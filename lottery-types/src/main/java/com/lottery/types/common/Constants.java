package com.lottery.types.common;

/**
 * @author 永
 * 常量
 */
public class Constants {

    public static final String SPLIT = ",";
    public static final String COLON = ":";
    public static final String SPACE = " ";
    public static final String UNDERLINE = "_";

    public static class RedisKey {
        public static final String ACTIVITY_KEY = "activity_of_";//缓存活动信息,需要在activity变更数据时删除
        public static final String ACTIVITY_SKU_COUNT_QUEUE_KEY = "activity_sku_count_queue_";//sku延迟队列
        public static final String STRATEGY_AWARD_COUNT_QUEUE_KEY = "strategy_award_count_queue_";//strategy_award延迟队列
        public static final String ACTIVITY_SKU_LIST_KEY = "activity_sku_list_";//缓存sku列表,需在变更sku数据时删除
        public static final String STRATEGY_AWARD_LIST_KEY = "strategy_award_list_";//缓存策略_奖品列表,需在变更strategy_award数据时删除
        public static final String STRATEGY_KEY = "strategy_of_";//缓存策略,需在变更strategy数据时删除
        public static final String STRATEGY_RULE_WEIGHT_KEY = "strategy_rule_weight_";//缓存策略规则权重,需在变更rule数据时删除
        public static final String ACTIVITY_SKU_STOCK_COUNT_KEY = "activity_sku_stock_count_";//缓存sku库存数量并对以扣减的库存进行上锁,需在变更sku数据时删除
        public static final String ACTIVITY_COUNT_KEY = "activity_count_of_";//缓存赠送的活动次数,需在变更activity_count数据时删除
        public static final String STRATEGY_AWARD_COUNT_KEY = "strategy_award_count_of_";//缓存某个strategy_award奖品数量,需在变更strategy_award数据时删除
        public static final String RATE_RANGE_KEY = "rate_range_";//缓存抽奖策略范围值,如1000，用于生成1000以内的随机数,需在变更strategy数据时删除
        public static final String RATE_TABLE_KEY = "rate_table_";//缓存概率查找表,按得到的随机值到表中取值即为抽到的奖,需在变更strategy数据时删除
        public static final String STRATEGY_AWARD_KEY = "strategy_award_of_";//缓存某个strategy_award奖品信息,需在变更strategy_award数据时删除
        public static final String RULE_TREE_KEY = "rule_tree_of_";//缓存某颗奖品规则数信息,需在变更rule_tree/rule_tree_node/rule_tree_node_line数据时删除
        public static final String ACTIVITY_ACCOUNT_LOCK = "activity_account_lock_";//活动账户锁,确保抽奖活动账户操作的原子性
        public static final String ACTIVITY_ACCOUNT_UPDATE_LOCK = "activity_account_update_lock_";//活动账户更新锁,确保活动账户更新操作的原子性
        public static final String CREDIT_ACCOUNT_LOCK = "credit_account_lock_";//积分账户锁,确保积分账户操作的原子性
        public static final String STRATEGY_ALGORITHM_KEY = "strategy_algorithm_";//缓存抽奖算法,在变更strategy时更新,在删除strategy时删除
        public static final String IS_RECEIVE_GIFT = "is_receive_gift_";//是否已经领取活动赠送的抽奖次数 键为返利id，值为用户id集合
        public static final String BLACKLIST = "blacklist_";
        public static final String RATE_LIMITER = "rate_limiter_";
    }

    public static class RuleModel {
        public static final String DEFAULT = "default";
        public static final String RULE_BLACKLIST = "rule_blacklist";
        public static final String RULE_WIGHT = "rule_weight";
        public static final String RULE_LOCK = "rule_lock";
        public static final String RULE_STOCK = "rule_stock";
        public static final String RULE_LUCK_AWARD = "rule_luck_award";
    }

    public static class ActivityModel {
        public static final String ACTIVITY_BASE = "activity_base";
        public static final String ACTIVITY_SKU_STOCK = "activity_sku_stock";

    }

    public static class AwardModel {
        public static final String USER_CREDIT_RANDOM = "user_credit_random";
        public static final String RULE_BLACKLIST = "user_blacklist";
    }

    public static class QuotaModel {
        public static final String CREDIT_PAY_TRADE = "credit_pay_trade";
        public static final String REBATE_NO_PAY_TRADE = "rebate_no_pay_trade";
        public static final String GIFT_NO_PAY_TRADE = "gift_no_pay_trade";

    }
    public static class Algorithm{
        public static final String O1 = "o1Algorithm";
        public static final String OLogN = "oLogNAlgorithm";
    }

}
