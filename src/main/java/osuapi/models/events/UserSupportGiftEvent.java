package osuapi.models.events;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserSupportGiftEvent extends Event {
    @JsonProperty("user")
    private EventUser user;
}