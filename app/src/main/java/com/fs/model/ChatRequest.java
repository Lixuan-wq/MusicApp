package com.fs.model;// 声明包名，指定该类所在的包路径

// 导入Java工具包中的List接口，用于定义消息列表
import java.util.List;

/**
 * 这个是我们向智谱AI模型发送的请求数据结构
 */
// 定义公共类ChatRequest，用于封装与AI模型交互的请求数据
public class ChatRequest {
    // 私有成员变量：模型名称，指定使用的AI模型
    private String model;

    // 私有成员变量：消息列表，包含对话的历史消息
    private List<Message> messages;

    // 私有成员变量：思考参数配置对象
    private Thinking thinking;

    // 私有成员变量：最大令牌数，限制AI响应的长度
    private int max_tokens;

    // 私有成员变量：温度参数，控制AI生成文本的随机性（0-1之间）
    private double temperature;

    /**
     * 构造方法，初始化ChatRequest对象
     * @param model AI模型名称
     * @param messages 对话消息列表
     * @param maxTokens 最大令牌数
     * @param temperature 温度参数
     */
    public ChatRequest(String model, List<Message> messages, int maxTokens, double temperature) {
        // 将传入的模型名称赋值给当前对象的model属性
        this.model = model;

        // 将传入的消息列表赋值给当前对象的messages属性
        this.messages = messages;

        // 将传入的最大令牌数赋值给当前对象的max_tokens属性
        this.max_tokens = maxTokens;

        // 将传入的温度参数赋值给当前对象的temperature属性
        this.temperature = temperature;
    }

    /**
     * 静态内部类：Message，表示单条对话消息
     */
    public static class Message {
        // 私有成员变量：角色类型，如"user"（用户）或"assistant"（助手）
        private String role;

        // 私有成员变量：消息内容，即实际的文本内容
        private String content;

        /**
         * 构造方法，初始化Message对象
         * @param role 消息角色
         * @param content 消息内容
         */
        public Message(String role, String content) {
            // 将传入的角色赋值给当前对象的role属性
            this.role = role;

            // 将传入的内容赋值给当前对象的content属性
            this.content = content;
        }

        /**
         * 获取消息角色
         * @return 角色字符串
         */
        public String getRole() {
            // 返回当前消息的角色
            return role;
        }

        /**
         * 设置消息角色
         * @param role 要设置的角色
         */
        public void setRole(String role) {
            // 将参数值赋给当前对象的role属性
            this.role = role;
        }

        /**
         * 获取消息内容
         * @return 消息内容字符串
         */
        public String getContent() {
            // 返回当前消息的内容
            return content;
        }

        /**
         * 设置消息内容
         * @param content 要设置的内容
         */
        public void setContent(String content) {
            // 将参数值赋给当前对象的content属性
            this.content = content;
        }
    }

    /**
     * 静态内部类：Thinking，表示思考相关的配置
     */
    public static class Thinking {
        // 私有成员变量：思考类型，指定思考的模式
        private String type;

        /**
         * 构造方法，初始化Thinking对象
         * @param type 思考类型
         */
        public Thinking(String type) {
            // 将传入的类型赋值给当前对象的type属性
            this.type = type;
        }

        /**
         * 获取思考类型
         * @return 类型字符串
         */
        public String getType() {
            // 返回当前的思考类型
            return type;
        }

        /**
         * 设置思考类型
         * @param type 要设置的类型
         */
        public void setType(String type) {
            // 将参数值赋给当前对象的type属性
            this.type = type;
        }
    }

    /**
     * 获取AI模型名称
     * @return 模型名称
     */
    public String getModel() {
        // 返回当前配置的模型名称
        return model;
    }

    /**
     * 设置AI模型名称
     * @param model 要设置的模型名称
     */
    public void setModel(String model) {
        // 将参数值赋给当前对象的model属性
        this.model = model;
    }

    /**
     * 获取消息列表
     * @return 消息列表
     */
    public List<Message> getMessages() {
        // 返回当前的消息列表
        return messages;
    }

    /**
     * 设置消息列表
     * @param messages 要设置的消息列表
     */
    public void setMessages(List<Message> messages) {
        // 将参数值赋给当前对象的messages属性
        this.messages = messages;
    }

    /**
     * 获取思考配置对象
     * @return Thinking对象
     */
    public Thinking getThinking() {
        // 返回当前的思考配置对象
        return thinking;
    }

    /**
     * 设置思考配置对象
     * @param thinking 要设置的Thinking对象
     */
    public void setThinking(Thinking thinking) {
        // 将参数值赋给当前对象的thinking属性
        this.thinking = thinking;
    }

    /**
     * 获取最大令牌数
     * @return 最大令牌数
     */
    public int getMax_tokens() {
        // 返回当前配置的最大令牌数
        return max_tokens;
    }

    /**
     * 设置最大令牌数
     * @param max_tokens 要设置的最大令牌数
     */
    public void setMax_tokens(int max_tokens) {
        // 将参数值赋给当前对象的max_tokens属性
        this.max_tokens = max_tokens;
    }

    /**
     * 获取温度参数
     * @return 温度参数值
     */
    public double getTemperature() {
        // 返回当前的温度参数
        return temperature;
    }

    /**
     * 设置温度参数
     * @param temperature 要设置的温度参数
     */
    public void setTemperature(double temperature) {
        // 将参数值赋给当前对象的temperature属性
        this.temperature = temperature;
    }
}
