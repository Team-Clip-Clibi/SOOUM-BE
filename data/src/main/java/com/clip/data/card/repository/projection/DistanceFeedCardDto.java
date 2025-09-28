package com.clip.data.card.repository.projection;

import org.geolatte.geom.Point;
import java.time.LocalDateTime;

public interface DistanceFeedCardDto {
    Long getPk();
    String getImgType();
    String getImgName();
    String getFont();
    String getContent();
    LocalDateTime getCreatedAt();
    boolean getIsStory();
    String getRole();
    Point getLocation();
}