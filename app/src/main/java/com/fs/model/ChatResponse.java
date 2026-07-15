// 声明包名，指定响应类所在的包路径
package com.fs.model;

// 导入List接口，用于定义选择项列表
import java.util.List;

/**
 * 智谱AI聊天接口响应对象
 * 对应API返回的JSON数据结构
 */
public class ChatResponse {
    // 响应的唯一标识符
    private String id;

    // 对象类型，通常为 "chat.completion"
    private String object;

    // 响应创建时的Unix时间戳（秒）
    private long created;

    // 实际使用的模型名称
    private String model;

    // 选择项列表，包含AI生成的回复内容
    private List<Choice> choices;

    // Token使用统计信息
    private Usage usage;

    /**
     * 静态内部类：Choice，表示AI生成的一个候选回复
     */
    public static class Choice {
        // 选择项在列表中的索引位置
        private int index;

        // AI生成的消息内容
        private Message message;

        // 完成原因，如 "stop"（正常结束）、"length"（达到最大长度）等
        private String finish_reason;

        /**
         * 获取选择项索引
         * @return 索引值
         */
        public int getIndex() {
            return index;
        }

        /**
         * 设置选择项索引
         * @param index 索引值
         */
        public void setIndex(int index) {
            this.index = index;
        }

        /**
         * 获取AI生成的消息
         * @return Message对象
         */
        public Message getMessage() {
            return message;
        }

        /**
         * 设置AI生成的消息
         * @param message Message对象
         */
        public void setMessage(Message message) {
            this.message = message;
        }

        /**
         * 获取完成原因
         * @return 完成原因字符串
         */
        public String getFinish_reason() {
            return finish_reason;
        }

        /**
         * 设置完成原因
         * @param finish_reason 完成原因字符串
         */
        public void setFinish_reason(String finish_reason) {
            this.finish_reason = finish_reason;
        }
    }

    /**
     * 静态内部类：Message，表示响应中的消息内容
     */
    public static class Message {
        // 消息角色，通常为 "assistant"
        private String role;

        // AI生成的文本内容
        private String content;

        /**
         * 获取消息角色
         * @return 角色字符串
         */
        public String getRole() {
            return role;
        }

        /**
         * 设置消息角色
         * @param role 角色字符串
         */
        public void setRole(String role) {
            this.role = role;
        }

        /**
         * 获取消息内容
         * @return 文本内容
         */
        public String getContent() {
            return content;
        }

        /**
         * 设置消息内容
         * @param content 文本内容
         */
        public void setContent(String content) {
            this.content = content;
        }
    }

    /**
     * 静态内部类：Usage，表示Token使用情况统计
     */
    public static class Usage {
        // 提示词消耗的Token数量
        private int prompt_tokens;

        // 生成内容消耗的Token数量
        private int completion_tokens;

        // 总Token消耗数量（prompt_tokens + completion_tokens）
        private int total_tokens;

        /**
         * 获取提示词Token数
         * @return Token数量
         */
        public int getPrompt_tokens() {
            return prompt_tokens;
        }

        /**
         * 设置提示词Token数
         * @param prompt_tokens Token数量
         */
        public void setPrompt_tokens(int prompt_tokens) {
            this.prompt_tokens = prompt_tokens;
        }

        /**
         * 获取生成内容Token数
         * @return Token数量
         */
        public int getCompletion_tokens() {
            return completion_tokens;
        }

        /**
         * 设置生成内容Token数
         * @param completion_tokens Token数量
         */
        public void setCompletion_tokens(int completion_tokens) {
            this.completion_tokens = completion_tokens;
        }

        /**
         * 获取总Token数
         * @return Token总数
         */
        public int getTotal_tokens() {
            return total_tokens;
        }

        /**
         * 设置总Token数
         * @param total_tokens Token总数
         */
        public void setTotal_tokens(int total_tokens) {
            this.total_tokens = total_tokens;
        }
    }

    /**
     * 获取响应ID
     * @return 响应唯一标识
     */
    public String getId() {
        return id;
    }

    /**
     * 设置响应ID
     * @param id 响应唯一标识
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取对象类型
     * @return 对象类型字符串
     */
    public String getObject() {
        return object;
    }

    /**
     * 设置对象类型
     * @param object 对象类型字符串
     */
    public void setObject(String object) {
        this.object = object;
    }

    /**
     * 获取创建时间戳
     * @return Unix时间戳
     */
    public long getCreated() {
        return created;
    }

    /**
     * 设置创建时间戳
     * @param created Unix时间戳
     */
    public void setCreated(long created) {
        this.created = created;
    }

    /**
     * 获取模型名称
     * @return 模型名称
     */
    public String getModel() {
        return model;
    }

    /**
     * 设置模型名称
     * @param model 模型名称
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * 获取选择项列表
     * @return Choice列表
     */
    public List<Choice> getChoices() {
        return choices;
    }

    /**
     * 设置选择项列表
     * @param choices Choice列表
     */
    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    /**
     * 获取Token使用统计
     * @return Usage对象
     */
    public Usage getUsage() {
        return usage;
    }

    /**
     * 设置Token使用统计
     * @param usage Usage对象
     */
    public void setUsage(Usage usage) {
        this.usage = usage;
    }
}
