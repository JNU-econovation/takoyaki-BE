package com.bestbenefits.takoyaki.service;

import com.bestbenefits.takoyaki.DTO.client.request.CommentReqDTO;
import com.bestbenefits.takoyaki.DTO.client.response.CommentIdResDTO;
import com.bestbenefits.takoyaki.DTO.client.response.CommentListResDTO;
import com.bestbenefits.takoyaki.entity.Comment;
import com.bestbenefits.takoyaki.entity.Party;
import com.bestbenefits.takoyaki.entity.User;
import com.bestbenefits.takoyaki.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final PartyService partyService;

    public CommentIdResDTO createComment(Long userId, Long partyId, CommentReqDTO dto) {
        User user = userService.getUserOrThrow(userId);
        Party party = partyService.getPartyOrThrow(partyId);

        Comment comment = commentRepository.save(dto.toEntity(user, party));

        return CommentIdResDTO.builder()
                .id(comment.getId())
                .build();
    }

    public List<CommentListResDTO> getComments(Long partyId) {
        Party party = partyService.getPartyOrThrow(partyId);
        List<Comment> found = commentRepository.getCommentsByParty(party);

        List<CommentListResDTO> ret = new ArrayList<>();
        for(Comment comment: found) {
            boolean isToday = comment.getCreatedAt().toLocalDate().isEqual(LocalDate.now());
            boolean isAuthor = comment.getUser() == party.getUser();

            String timeStamp = comment.getCreatedAt().format(DateTimeFormatter.ofPattern(
                    isToday ? "HH:mm" : "yyyy-MM-dd HH:mm"));

            ret.add(CommentListResDTO.builder()
                    .createdAt(timeStamp)
                    .nickname(comment.getUser().getNickname())
                    .comment(comment.getBody())
                    .isAuthor(isAuthor)
                    .build());
        }

        return ret;
    }
}
