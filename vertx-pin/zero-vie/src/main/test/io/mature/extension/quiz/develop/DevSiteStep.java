package io.mature.extension.quiz.develop;

class DevSiteStep {
    private final Integer step;
    private Integer start = 0;

    DevSiteStep(final Integer step) {
        this.step = step;
    }

    void moveOn() {
        this.start = this.start + this.step;
        // return this;
    }

    Integer value() {
        return this.start;
    }

    void jump(final Integer start) {
        this.start = start;
        // return this;
    }
}
