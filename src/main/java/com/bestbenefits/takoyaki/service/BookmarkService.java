package com.bestbenefits.takoyaki.service;

import com.bestbenefits.takoyaki.entity.Bookmark;
import com.bestbenefits.takoyaki.entity.Party;
import com.bestbenefits.takoyaki.entity.User;
import com.bestbenefits.takoyaki.repository.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final UserService userService;
    private final PartyService partyService;
    public List<Party> getBookmarkedPartiesByUser(Long userId) {
        User user = userService.getUserOrThrow(userId);
        return bookmarkRepository.getBookmarksByUser(user).stream()
                .map(Bookmark::getParty)
                .collect(Collectors.toList());
    }

    public void addBookmark(Long userId, Long partyId) {
        User user = userService.getUserOrThrow(userId);
        Party party = partyService.getPartyOrThrow(partyId);

        //이미 북마크된 경우
        if (bookmarkRepository.findByUserAndParty(user, party).isPresent())
            throw new IllegalArgumentException("이미 북마크 되었습니다.");

        //타코가 자신의 게시물 북마크하는 경우
        if (party.getUser() == user)
            throw new IllegalArgumentException("자신의 글을 북마크할 수 없습니다.");

        //북마크 개수 20개 초과시 북마크 안됨(정책에 의거)
        if (getBookmarkedPartiesByUser(userId).size() >= 20)
            throw new IllegalStateException("북마크는 최대 20개까지 가능합니다.");

        //마감된 글 북마크하는 경우
        if (party.isClosed())
            throw new IllegalStateException("마감된 글은 북마크할 수 없습니다.");

        //삭제된 글 북마크하는 경우
        if (party.isDeleted())
            throw new IllegalStateException("삭제된 글은 북마크할 수 없습니다.");

        bookmarkRepository.save(Bookmark.builder()
                .user(user)
                .party(party)
                .build());
    }

    public void deleteBookmark(Long userId, Long partyId) {
        User user = userService.getUserOrThrow(userId);
        Party party = partyService.getPartyOrThrow(partyId);

        Optional<Bookmark> byUserAndParty = bookmarkRepository.findByUserAndParty(user, party);
        if (byUserAndParty.isPresent())
            bookmarkRepository.delete(byUserAndParty.get());
        else
            throw new IllegalArgumentException("북마크가 존재하지 않습니다.");
    }

    public void deleteBookmarksByParty(Long partyId) {
        Party party = partyService.getPartyOrThrow(partyId);
        bookmarkRepository.deleteAllByParty(party);
    }
}
