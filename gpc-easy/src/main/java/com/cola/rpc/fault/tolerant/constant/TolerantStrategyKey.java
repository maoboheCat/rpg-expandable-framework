package com.cola.rpc.fault.tolerant.constant;

/**
 * 容错策略键名常量
 * @author Maobohe
 * @createData 2024/4/1 19:45
 */
public interface TolerantStrategyKey {

    /**
     * 快速失败
     */
    String FAIL_FAST = "failFast";

    /**
     * 默认处理
     */
    String FAIL_SAFE = "failSafe";

    /**
     * 失败自动恢复
     */
    String FAIL_BACK = "failBack";

    /**
     * 故障转义
     */
    String FAIL_OVER = "failOver";
}
