package com.example.taskflow.domain.comment.repository;

import com.example.taskflow.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Task별 최대 groupId 조회
     * 사용 시점 : 새로운 최상위 댓글을 만들 때
     * @param taskId
     * @return
     */
    @Query("SELECT COALESCE(MAX(c.groupId), 0) FROM Comment c WHERE c.task.id = :taskId")
    Long findMaxGroupIdByTaskId(Long taskId);

    /**
     * 같은 그룹 내 가장 큰 시퀀스 찾기
     * 사용 시점: 대댓글 만들 때
     * groupId=1의 최대 sequence
     * @param groupId
     * @return
     */
    @Query("SELECT COALESCE(MAX(c.sequence), 0) FROM Comment c WHERE c.groupId = :groupId")
    Long findMaxSequenceByGroupId(Long groupId);
}
