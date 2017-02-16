package com.nowcoder.dao;

import com.nowcoder.model.Comment;
import com.nowcoder.model.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Project: wenda
 * Author: Chow xi
 * Email: zhouxu_1994@163.com
 * Time: 17/2/16 下午1:00
 */
@Mapper
public interface MessageDAO {

    String TABLE_NAME = " message ";
    String INSERT_FIELDS = " from_id, to_id, content, created_date, has_read, conversation_id ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{fromId},#{toId},#{content},#{createdDate},#{hasRead}, #{conversationId})"})
    int addMessage(Message Message);

    @Select({"select", SELECT_FIELDS, "from", TABLE_NAME, "where conversation_id = #{conversationId} limit #{offset}, #{limit}"})
    List<Message> getConversationDetail(@Param("conversationId") String conversationId,
                                        @Param("offset") int offset,
                                        @Param("limit") int limit);


    @Select({"select", INSERT_FIELDS, ", count(id) as id from",
            TABLE_NAME, "where from_id = #{userId} or to_id = #{userId} group by conversation_id desc limit #{offset}, #{limit}"})
    List<Message> getConversationList(@Param("userId") int userId,
                                      @Param("offset") int offset,
                                      @Param("limit") int limit);


    @Select({"select count(id) from", TABLE_NAME, "where to_id = #{userId} and conversation_id = #{conversationId} and has_read = 0"})
    int getConversationUnreadCount(@Param("userId") int userId, @Param("conversationId") String conversationId);

    @Update({"update", TABLE_NAME, "set has_read = 1 where conversation_id = #{conversationId} and to_id = #{userId}"})
    int updateHasReadStatus(@Param("conversationId") String  conversationId, @Param("userId") int userId);

}
