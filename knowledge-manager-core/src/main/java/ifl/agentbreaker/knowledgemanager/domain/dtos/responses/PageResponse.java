package ifl.agentbreaker.knowledgemanager.domain.dtos.responses;

import lombok.Data;

import java.util.List;

@Data
public class PageResponse<T>
{
    /**
     * 总数量
     */
    private long total;

    /**
     * 当前页
     */
    private int pageNumber;

    /**
     * 每页数量
     */
    private int pageSize;

    /**
     * 数据
     */
    private List<T> records;
}
