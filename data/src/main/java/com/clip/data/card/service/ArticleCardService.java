package com.clip.data.card.service;
import com.clip.data.card.entity.ArticleCard;
import com.clip.data.card.entity.FeedCard;
import com.clip.data.card.repository.ArticleCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class ArticleCardService {

    private final ArticleCardRepository articleCardRepository;

    public ArticleCard saveArticleCard(Long feedCardPk) {
        ArticleCard articleCard = ArticleCard.builder()
                .feedCardPk(feedCardPk)
                .build();
        return articleCardRepository.save(articleCard);
    }

    public Optional<FeedCard> findLatestArticleFeedCard() {
        return articleCardRepository.findLatestArticleFeedCard();
    }

    public boolean isArticleCard(Long feedCardPk) {
        return articleCardRepository.existsByFeedCardPk(feedCardPk);
    }
}
