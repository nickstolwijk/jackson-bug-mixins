package nl.stolwijk.nick.jackson.mixin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class ObjectMapperTest {

    private ObjectMapper objectMapper;

    private Human jim;
    private Muppet kermit;

    private Muppet animal;

    @BeforeEach
    void initObjectMapper() {
        objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        jim = new Human("Jim", "Henson");
        kermit = new Muppet("Kermit the Frog");
        animal = new Muppet("Animal");
    }

    @Test
    void objectMapper_should_map_muppet() throws JsonProcessingException {
        final String kermitJson = objectMapper.writeValueAsString(kermit);

        assertThat(kermitJson).contains("Kermit the Frog");
    }

    @Test
    void objectMapper_should_map_human() throws JsonProcessingException {
        final String jimJson = objectMapper.writeValueAsString(jim);

        assertThat(jimJson).contains("Jim");
    }

    @Test
    void objectMapper_should_map_show() throws JsonProcessingException {
        var show = Show.builder()
                .mainMuppet(kermit)
                .mainHuman(jim)
                .build();

        final String showJson = objectMapper.writeValueAsString(show);

        assertThat(showJson)
                .contains("Jim")
                .contains("Kermit the Frog");
    }

    @Test
    void objectMapper_should_skip_muppet() throws JsonProcessingException {
        objectMapper.addMixIn(Muppet.class, MuppetMixin.class);

        var show = Show.builder()
                .mainMuppet(kermit)
                .build();

        final String showJson = objectMapper.writeValueAsString(show);

        assertThat(showJson)
                .doesNotContain("Kermit the Frog");
    }

    @Test
    void objectMapper_should_skip_muppet_in_list() throws JsonProcessingException {
        objectMapper.addMixIn(Muppet.class, MuppetMixin.class);

        var show = Show.builder()
                .muppet(animal)
                .build();

        final String showJson = objectMapper.writeValueAsString(show);

        assertThat(showJson)
                .doesNotContain("Animal");
    }

    @Test
    void objectMapper_should_skip_muppet_in_map() throws JsonProcessingException {
        objectMapper.addMixIn(Muppet.class, MuppetMixin.class);

        var show = Show.builder()
                .puppeteer("Frank Oz", animal)
                .build();

        final String showJson = objectMapper.writeValueAsString(show);

        assertThat(showJson)
                .doesNotContain("Animal");
    }

    @Test
    void objectMapper_should_skip_human_name() throws JsonProcessingException {
        objectMapper.addMixIn(Human.class, HumanMixin.class);

        var show = Show.builder()
                .mainHuman(jim)
                .build();

        final String showJson = objectMapper.writeValueAsString(show);

        assertThat(showJson)
                .doesNotContain("Jim")
                .contains("Henson");
    }

    @Test
    void objectMapper_should_skip_human_name_in_list() throws JsonProcessingException {
        objectMapper.addMixIn(Human.class, HumanMixin.class);

        var show = Show.builder()
                .castMember(jim)
                .build();

        final String showJson = objectMapper.writeValueAsString(show);

        assertThat(showJson)
                .doesNotContain("Jim")
                .contains("Henson");
    }

    static class HumanMixin {
        @JsonIgnore
        String firstName;
    }

    @JsonIgnoreType
    static class MuppetMixin {

    }
}