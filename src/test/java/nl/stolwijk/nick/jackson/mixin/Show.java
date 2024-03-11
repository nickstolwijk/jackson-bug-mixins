package nl.stolwijk.nick.jackson.mixin;

import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
final  class Show {
    private Muppet mainMuppet;
    private Human mainHuman;

    @Singular
    private final List<Muppet> muppets;

    @Singular
    private final Map<String, Muppet> puppeteers;

    @Singular
    private final List<Human> castMembers;
}
