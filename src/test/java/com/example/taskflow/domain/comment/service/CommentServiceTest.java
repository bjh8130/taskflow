package com.example.taskflow.domain.comment.service;

import com.example.taskflow.common.exception.CustomException;
import com.example.taskflow.common.exception.ErrorCode;
import com.example.taskflow.common.response.CustomPageResponse;
import com.example.taskflow.domain.comment.dto.request.CommentCreateRequestDto;
import com.example.taskflow.domain.comment.dto.request.CommentUpdateRequestDto;
import com.example.taskflow.domain.comment.dto.response.CommentGetResponseDto;
import com.example.taskflow.domain.comment.dto.response.CommentResponseDto;
import com.example.taskflow.domain.comment.dto.response.CommentUpdateResponseDto;
import com.example.taskflow.domain.comment.entity.Comment;
import com.example.taskflow.domain.comment.repository.CommentRepository;
import com.example.taskflow.domain.task.entity.Task;
import com.example.taskflow.domain.task.repository.TaskRepository;
import com.example.taskflow.domain.user.entity.User;
import com.example.taskflow.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("CommentService 테스트")
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CommentService commentService;

    private User testUser;
    private Task testTask;
    private Comment testComment;
    private Comment testParentComment;

    @BeforeEach
    void setUp() {
        // 테스트용 User 생성
        testUser = new User("testuser", "test@example.com", "Test User", "password123");

        // 테스트용 Task는 Mock 사용
        testTask = mock(Task.class);
        when(testTask.getId()).thenReturn(1L);

        // 테스트용 부모 댓글 생성
        testParentComment = Comment.builder()
                .content("부모 댓글")
                .task(testTask)
                .user(testUser)
                .groupId(1L)
                .sequence(0L)
                .depth(0L)
                .build();

        // 테스트용 댓글 생성
        testComment = Comment.builder()
                .content("테스트 댓글")
                .task(testTask)
                .user(testUser)
                .groupId(1L)
                .sequence(0L)
                .depth(0L)
                .build();
    }

    @Test
    @DisplayName("댓글 생성 성공")
    void createComment_Success() {
        // given
        Long taskId = 1L;
        Long userId = 1L;
        CommentCreateRequestDto request = new CommentCreateRequestDto("새 댓글", null);

        given(userRepository.findById(userId)).willReturn(Optional.of(testUser));
        given(taskRepository.findById(taskId)).willReturn(Optional.of(testTask));
        given(commentRepository.findMaxGroupIdByTaskId(taskId)).willReturn(0L);
        given(commentRepository.save(any(Comment.class))).willReturn(testComment);

        // when
        CommentResponseDto result = commentService.createComment(request, taskId, userId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEqualTo("테스트 댓글");
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    @DisplayName("댓글 생성 실패 - 사용자 없음")
    void createComment_UserNotFound() {
        // given
        Long taskId = 1L;
        Long userId = 999L;
        CommentCreateRequestDto request = new CommentCreateRequestDto("새 댓글", null);

        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> commentService.createComment(request, taskId, userId))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.USER_NOT_FOUND);
    }

    @Test
    @DisplayName("댓글 생성 실패 - 작업 없음")
    void createComment_TaskNotFound() {
        // given
        Long taskId = 999L;
        Long userId = 1L;
        CommentCreateRequestDto request = new CommentCreateRequestDto("새 댓글", null);

        given(userRepository.findById(userId)).willReturn(Optional.of(testUser));
        given(taskRepository.findById(taskId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> commentService.createComment(request, taskId, userId))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.TASK_NOT_FOUND);
    }

    @Test
    @DisplayName("대댓글 생성 성공")
    void createReply_Success() {
        // given
        Long taskId = 1L;
        Long userId = 1L;
        Long parentId = 1L;
        CommentCreateRequestDto request = new CommentCreateRequestDto("대댓글", parentId);

        Comment reply = Comment.builder()
                .content("대댓글")
                .task(testTask)
                .user(testUser)
                .comment(testParentComment)
                .groupId(1L)
                .sequence(1L)
                .depth(1L)
                .build();

        given(userRepository.findById(userId)).willReturn(Optional.of(testUser));
        given(taskRepository.findById(taskId)).willReturn(Optional.of(testTask));
        given(commentRepository.findById(parentId)).willReturn(Optional.of(testParentComment));
        given(commentRepository.findMaxSequenceByGroupId(1L)).willReturn(0L);
        given(commentRepository.save(any(Comment.class))).willReturn(reply);

        // when
        CommentResponseDto result = commentService.createComment(request, taskId, userId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEqualTo("대댓글");
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    @DisplayName("댓글 목록 조회 성공")
    void getComments_Success() {
        // given
        Long taskId = 1L;
        int page = 0;
        int size = 10;
        String sort = "newest";

        List<Comment> comments = List.of(testComment, testParentComment);
        Page<Comment> commentPage = new PageImpl<>(comments, PageRequest.of(page, size), comments.size());

        given(commentRepository.findByTaskIdWithUserAndTask(eq(taskId), any(Pageable.class)))
                .willReturn(commentPage);

        // when
        CustomPageResponse<CommentGetResponseDto> result = commentService.getComments(taskId, page, size, sort);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        verify(commentRepository, times(1)).findByTaskIdWithUserAndTask(eq(taskId), any(Pageable.class));
    }

    @Test
    @DisplayName("댓글 수정 성공")
    void updateComment_Success() {
        // given
        Long taskId = 1L;
        Long commentId = 1L;
        Long userId = 1L;
        CommentUpdateRequestDto request = new CommentUpdateRequestDto("수정된 댓글");

        // User의 ID를 설정하기 위해 Mock 사용
        User mockUser = mock(User.class);
        when(mockUser.getId()).thenReturn(userId);

        Comment commentWithUser = Comment.builder()
                .content("원본 댓글")
                .task(testTask)
                .user(mockUser)
                .groupId(1L)
                .sequence(0L)
                .depth(0L)
                .build();

        given(commentRepository.findById(commentId)).willReturn(Optional.of(commentWithUser));
        given(commentRepository.save(any(Comment.class))).willReturn(commentWithUser);

        // when
        CommentUpdateResponseDto result = commentService.updateComment(taskId, commentId, request, userId);

        // then
        assertThat(result).isNotNull();
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    @DisplayName("댓글 수정 실패 - 댓글 없음")
    void updateComment_CommentNotFound() {
        // given
        Long taskId = 1L;
        Long commentId = 999L;
        Long userId = 1L;
        CommentUpdateRequestDto request = new CommentUpdateRequestDto("수정된 댓글");

        given(commentRepository.findById(commentId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> commentService.updateComment(taskId, commentId, request, userId))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.COMMENT_NOT_FOUND);
    }

    @Test
    @DisplayName("댓글 수정 실패 - 권한 없음")
    void updateComment_Forbidden() {
        // given
        Long taskId = 1L;
        Long commentId = 1L;
        Long userId = 1L;
        Long otherUserId = 2L;
        CommentUpdateRequestDto request = new CommentUpdateRequestDto("수정된 댓글");

        User mockUser = mock(User.class);
        when(mockUser.getId()).thenReturn(otherUserId); // 다른 사용자 ID

        Comment commentWithUser = Comment.builder()
                .content("원본 댓글")
                .task(testTask)
                .user(mockUser)
                .groupId(1L)
                .sequence(0L)
                .depth(0L)
                .build();

        given(commentRepository.findById(commentId)).willReturn(Optional.of(commentWithUser));

        // when & then
        assertThatThrownBy(() -> commentService.updateComment(taskId, commentId, request, userId))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.COMMENT_FORBIDDEN);
    }

    @Test
    @DisplayName("댓글 삭제 성공")
    void deleteComment_Success() {
        // given
        Long commentId = 1L;
        Long userId = 1L;

        User mockUser = mock(User.class);
        when(mockUser.getId()).thenReturn(userId);

        Comment commentWithUser = Comment.builder()
                .content("삭제할 댓글")
                .task(testTask)
                .user(mockUser)
                .groupId(1L)
                .sequence(0L)
                .depth(0L)
                .build();

        given(commentRepository.findById(commentId)).willReturn(Optional.of(commentWithUser));
        doNothing().when(commentRepository).delete(any(Comment.class));

        // when
        commentService.deleteComment(commentId, userId);

        // then
        verify(commentRepository, times(1)).delete(any(Comment.class));
    }

    @Test
    @DisplayName("댓글 삭제 실패 - 댓글 없음")
    void deleteComment_CommentNotFound() {
        // given
        Long commentId = 999L;
        Long userId = 1L;

        given(commentRepository.findById(commentId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> commentService.deleteComment(commentId, userId))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.COMMENT_NOT_FOUND);
    }

    @Test
    @DisplayName("댓글 삭제 실패 - 권한 없음")
    void deleteComment_Forbidden() {
        // given
        Long commentId = 1L;
        Long userId = 1L;
        Long otherUserId = 2L;

        User mockUser = mock(User.class);
        when(mockUser.getId()).thenReturn(otherUserId); // 다른 사용자 ID

        Comment commentWithUser = Comment.builder()
                .content("삭제할 댓글")
                .task(testTask)
                .user(mockUser)
                .groupId(1L)
                .sequence(0L)
                .depth(0L)
                .build();

        given(commentRepository.findById(commentId)).willReturn(Optional.of(commentWithUser));

        // when & then
        assertThatThrownBy(() -> commentService.deleteComment(commentId, userId))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.COMMENT_DELETE_FORBIDDEN);
    }
}