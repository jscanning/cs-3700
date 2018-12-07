package rockPaperScissors;

public enum Hand 
{
	ROCK, PAPER, SCISSORS;
	
	enum Result {WIN, LOSE, DRAW}
	
	public Result evaluate(Hand other)
	{
		switch (this) {
		case ROCK:
			if(other == Hand.SCISSORS) return Result.WIN;
			if(other == Hand.PAPER) return Result.LOSE;
		case PAPER:
			if(other == Hand.ROCK) return Result.WIN;
			if(other == Hand.SCISSORS) return Result.LOSE;
		case SCISSORS:
			if(other == Hand.PAPER) return Result.WIN;
			if(other == Hand.ROCK) return Result.LOSE;
		}
		return Result.DRAW;
	}
}
