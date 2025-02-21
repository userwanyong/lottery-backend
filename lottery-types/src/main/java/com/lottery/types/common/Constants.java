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
        public static final String RULE_TREE_VO_KEY = "lottery_rule_tree_vo_key_";
        public static final String STRATEGY_KEY = "lottery_strategy_key_";
        public static final String STRATEGY_AWARD_KEY = "lottery_strategy_award_key_";
        public static final String STRATEGY_AWARD_LIST_KEY = "lottery_strategy_award_list_key_";
        public static final String STRATEGY_RATE_TABLE_KEY = "lottery_strategy_rate_table_key_";
        public static final String STRATEGY_RATE_RANGE_KEY = "lottery_strategy_rate_range_key_";
        public static final String STRATEGY_AWARD_COUNT_KEY = "lottery_strategy_award_count_key_";
        public static final String STRATEGY_AWARD_COUNT_QUEUE_KEY = "lottery_strategy_award_count_queue_key_";
    }

    public static class RuleModel {
        public static final String DEFAULT = "default";
        public static final String RULE_BLACKLIST = "rule_blacklist";
        public static final String RULE_WIGHT = "rule_weight";
        public static final String RULE_LOCK = "rule_lock";
        public static final String RULE_STOCK = "rule_stock";
        public static final String RULE_LUCK_AWARD = "rule_luck_award";
    }

}
