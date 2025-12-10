package com.example.taskflow.domain.comment.repository;

import com.example.taskflow.domain.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    //Task별 댓글 페이징 조회
    Page<Comment> findByTaskId(Long taskId, Pageable pageable);

    //Task별 댓글 페이징 조회 (fetch join으로 User, Task 함께 조회)
    @Query(value = "SELECT c FROM Comment c " +
                   "JOIN FETCH c.user " +
                   "JOIN FETCH c.task " +
                   "WHERE c.task.id = :taskId",
           countQuery = "SELECT COUNT(c) FROM Comment c WHERE c.task.id = :taskId")
    Page<Comment> findByTaskIdWithUserAndTask(Long taskId, Pageable pageable);
}
