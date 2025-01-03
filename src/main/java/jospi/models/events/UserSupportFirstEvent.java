package jospi.models.events;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserSupportFirstEvent extends Event {
    @JsonProperty("user")
    private EventUser user;
}
