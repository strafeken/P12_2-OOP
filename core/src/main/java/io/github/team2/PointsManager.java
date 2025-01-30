package io.github.team2;

public class PointsManager {
	
	private int points;
	
	public PointsManager()
	{
		points = 0;
	}
	
	public int getPoints()
	{
		return points;
	}
	
	public void addPoints(int value)
	{
		points += value;
	}
	
	public void subtractPoints(int value)
	{
		points -= value;
	}
	
	public void resetPoints()
	{
		points = 0;
	}
	
	public void savePoints()
	{
		
	}
	
	public void loadPoints()
	{
		
	}
}
