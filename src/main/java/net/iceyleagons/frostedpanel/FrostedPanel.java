package net.iceyleagons.frostedpanel;

import lombok.Getter;
import lombok.Setter;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @deprecated not yet suppoted nor implemented fully
 * @author TOTHTOMI
 */
@Deprecated
public class FrostedPanel {

    @Getter
    private final String name;
    @Getter
    private final String description;
    @Getter
    private final Double requiredPanelVersion;
    @Getter
    private final List<Step> steps;

    public FrostedPanel(String name, String description, Double requiredPanelVersion,List<Step> steps) {
        this.name = name;
        this.description = description;
        this.requiredPanelVersion = requiredPanelVersion;
        this.steps = steps;
    }

    private String writeToBytebin(String data) {
        //TODO
        return "key";
    }

    private String readFromBytebin(String key) {
        //TODO
        return "data";
    }

    private String generateJSON() {
        //TODO
        return "json";
    }

    public void fetchAnswers() {

    }

    public String getPanelURL() {
        return "https://panel.iceyleagons.net/?id=";
    }


    public static class Builder {

        @Getter
        private String name, description;
        @Getter
        private Double requiredPanelVersion = 1.0; //Default to the newest version
        private List<Step> steps;

        public Builder(String name) {
            this.name = name;
            this.steps = new ArrayList<>();
            this.description = "";
        }

        public FrostedPanel build() {
            return new FrostedPanel(name,description,requiredPanelVersion,steps);
        }


        /**
         *
         * @param step {@link Step} to add
         * @return the {@link Builder} for chaining
         */
        public Builder addStep(Step step) {
            steps.add(step);
            return this;
        }

        /**
         * @param value to set the requiredPanelVersion to (default is the newest)
         * @return the {@link Builder} for chaining
         */
        public Builder setRequiredPanelVersion(Double value) {
            this.requiredPanelVersion = value;
            return this;
        }

        /**
         * @param description the description of the panel
         * @return the {@link Builder} for chaining
         */
        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        /**
         *
         * @param name the name of the panel
         * @return the {@link Builder} for chaining
         */
        public static Builder createPanel(String name) {
            return new Builder(name);
        }
    }


    public static class Step {

        @Getter
        private String name,description;
        private List<Input> inputs;

        /**
         * @param name is the name of the step (Display on the progress bar)
         */
        public Step(String name) {
            this.name = name;
            this.inputs = new ArrayList<>();
        }

        /**
         * @param description is the description of the {@link Step}
         * @return the {@link Step} for chaining
         */
        public Step setDescription(String description) {
            this.description = description;
            return this;
        }

        /**
         * @param input to add
         * @return the {@link Step} for chaining
         */
        public Step addInput(Input input) {
            inputs.add(input);
            return this;
        }
    }

    public static class Input {

        @Getter
        private final InputType inputType;
        @Getter
        private final String label;
        @Getter
        private final String placeholder;
        @Getter
        private final UUID uuid;
        @Getter
        @Setter
        private String answer;

        public Input(InputType inputType, String label, String placeholder) {
            this.inputType = inputType;
            this.label = label;
            this.placeholder = placeholder;
            this.uuid = UUID.randomUUID();
        }

    }

    public enum InputType{
        TEXT("txt"),NUMBER("nbr"),EMAIL("eml"),SWITCH("swc");

        @Getter
        private String dictionaryMark;
        InputType(String dictionaryMark) {
            this.dictionaryMark = dictionaryMark;
        }
    }
}
