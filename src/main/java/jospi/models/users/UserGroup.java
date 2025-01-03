package jospi.models.users;

import com.fasterxml.jackson.annotation.JsonProperty;

import jospi.enums.misc.Ruleset;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserGroup extends Group {

    @JsonProperty("playmodes")
    private Ruleset[] playModes;
}
