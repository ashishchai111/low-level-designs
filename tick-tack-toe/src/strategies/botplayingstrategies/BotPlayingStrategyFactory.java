package strategies.botplayingstrategies;

import models.BotDifficultyLevel;

public class BotPlayingStrategyFactory {

    public static BotPlayingStrategy getBotPlayingStrategyForDifficultyLevel(BotDifficultyLevel botDifficultyLevel) {
        return new EasyBotPlayingStrategy();
        /*return switch (botDifficultyLevel) {
            case MEDIUM -> new MediumBotPlayingStrategy();
            case HARD -> new HardBotPlayingStrategy();
            default -> new EasyBotPlayingStrategy();
        };*/
    }
}
