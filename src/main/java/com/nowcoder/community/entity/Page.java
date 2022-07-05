package com.nowcoder.community.entity;

/**
 * @Author GuoDingWei
 * @Date 2022/6/14 15:34
 */
/**
 * 封装分页相关的信息.
 */
public class Page {

    // 当前页码
    private int current = 1;
    // 显示上限
    private int limit = 10;
    // 数据总数(用于计算总页数)
    private int rows;
    // 查询路径(用于复用分页链接)
    private String path;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        if (current >= 1 && current <= 100) {
            this.current = current;
        }
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if (limit >= 1 && limit <= 100) {
            this.limit = limit;
        }
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        if (rows >= 0) {
            this.rows = rows;
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取当前页的起始行
     * @return 获取当前页的起始行
     */
    public int getOffset() {
        // current * limit - limit
        return (current - 1) * limit;
    }

    /**
     * 获取总页数
     * rows表示数据的总数量，limit表示每页显示的数量
     * @return 获取总页数
     */
    public int getTotal() {
        // rows / limit [+1]
        if (rows % limit == 0) {
            return rows / limit;
        } else {
            return rows / limit + 1;
        }
    }

    /**
     * 获取起始页码
     * 这里就是显示获取当前页的前两页，一个显示5页
     * @return 获取起始页码
     */
    public int getFrom() {
        int from = current - 2;
        //如果小于两页，就显示前1页
        return Math.max(from, 1);
    }

    /**
     * 获取结束页码
     * 这里就是显示获取当前页的后两页，一个显示5页
     * @return 获取结束页码
     */
    public int getTo() {
        int to = current + 2;
        int total = getTotal();
        //如果大于了最后的一页，就显示最后的一页
        return Math.min(to, total);
    }
}
