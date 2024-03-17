package com.scilab.giftslist.api.offering.model;

import java.time.LocalDateTime;

import com.scilab.giftslist.api.discussion.model.DiscussionModel;
import com.scilab.giftslist.api.gift.models.GiftModel;
import com.scilab.giftslist.api.glist.model.GiftListModel;
import com.scilab.giftslist.api.user.model.UserModel;
import com.scilab.giftslist.domain.offering.model.Offering;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class OfferingModel {
    private UserModel giver;
    private UserModel reciever;
    private LocalDateTime givenDate;
    private GiftListModel list;
    private GiftModel gift;
    private DiscussionModel discussion;
    
    
    public static OfferingModel fromOffering(Offering offering){
        return OfferingModel.builder()
            .giver(UserModel.builder()
                .id(offering.getGiver().getId())
                .username(offering.getGiver().getUsername())
                .usagename(offering.getGiver().usageName())
                .pictureId(offering.getGiver().getProfilePicture()==null ? null : offering.getGiver().getProfilePicture().getId())
                .build())
            .reciever(UserModel.builder()
                .username(offering.getReciever().getUsername())
                .usagename(offering.getReciever().usageName())
                .pictureId(offering.getReciever().getProfilePicture()==null ? null : offering.getReciever().getProfilePicture().getId())
                .build())
            .givenDate(offering.getGivenDate())
            .list(GiftListModel.builder()
                    .id(offering.getOwningList()== null ? null : offering.getOwningList().getId())
                    .title(offering.getOwningList().getName())
                .build())
            .gift(GiftModel.builder()
                .title(offering.getTitle())
                .rating(offering.getRating())
                .comment(offering.getComment())            
                .build())
            .discussion(DiscussionModel.builder()
                .id(offering.getDiscussion()==null ? null : offering.getDiscussion().getId())
                .build())
            .build();
    }
}
