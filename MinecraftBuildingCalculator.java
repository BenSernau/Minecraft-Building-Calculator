import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;

class BuildingSegment
{
	boolean twoStaircases;
	int length, width, height, storeys, floorArea, wallArea, stairNum; //length, width, and height are for each individual storey; storeys is for how many storeys you'd like the former three parameters to persist.

	private int calculateFloor(int l, int w)
	{
		System.out.println("The number of blocks necessary for each floor in this segment is " + Integer.toString((((l - 2) * (w - 2)) + (((l - 2) * (w - 2)) % 2)) / 2) + " (floors are made from slabs).\n");
		return ((((l - 2) * (w - 2)) + (((l - 2) * (w - 2)) % 2)) / 2); //One block turns into two slabs.  I *added* the remainder since, for purposes of building something, having too many slabs is better than having too few.
	}

	private int calculateWalls(int l, int w, int h) //The height of the wall is *between* the floor and one block above the ceiling/next floor. In other words, the height "includes" the ceiling/next floor.
	{
		System.out.println("The number of blocks necessary for each 4-wall set in this segment is " + Integer.toString((l * w - ((l - 2) * (w - 2))) * h) + ".\n");
		return ((l * w - ((l - 2) * (w - 2))) * h);
	}

	private int calculateStairs(int h, int s, boolean twoStaircases)
	{
		if (twoStaircases)
		{
			System.out.println("The number of blocks necessary for the stairs in this segment is " + Long.toString(Math.round(2 * (3 * h - 3.5) * s)) + ". If this segment is the last one in the building, you need " + Long.toString(Math.round(2 * ((3 * h - 3.5) * (s - 1)))) + " blocks.\n");
			return ((int) Math.round(2 * (3 * h - 3.5) * s));
		}

		else
		{
			System.out.println("The number of blocks necessary for the stairs in this segment is " + Long.toString(Math.round((3 * h - 3.5) * s)) + ". If this segment is the last one in the building, you need " + Long.toString(Math.round((3 * h - 3.5) * (s - 1))) + " blocks.\n"); 
			return ((int) Math.round((3 * h - 3.5) * s));
		}
	}

	BuildingSegment(int l, int w, int h, int s, boolean twoStairs)
	{
		System.out.println("\nCreating segment...\n");

		if (w > l)
		{
			width = l;
			length = w;
		}

		else
		{
			length = l;
			width = w;
		}

		height = h;
		storeys = s;
		floorArea = calculateFloor(l, w);
		wallArea = calculateWalls(l, w, h);

		if (s > 1)
		{
			System.out.println("Since there are " + s + " storeys, each floor in the segment adds up to " + Integer.toString(floorArea * s) + " blocks, and each set of 4 walls adds up to " + Integer.toString(wallArea * s) + " blocks.\n");
		}

		twoStaircases = twoStairs;
		stairNum = calculateStairs(h, s, twoStaircases);
	}
}

class MinecraftBuildingCalculator
{
	private static int standardRoof(int l, int w)	//You can swap l and w depending on the direction you'd like your roof to face.
	{
		int roofNum = 3 * l * w - 3 * l - 3 * w + 6;

		if (w % 2 != 0)
		{
			roofNum += (int) Math.round(2 - 2.5 * l); //The formula for finding the number of blocks necessary for building this roof, if the width of the building is odd, is 3lw - 5.5l - 3w + 8.
		}

		for (int i = w - 2; i > 0; i -= 2)
		{
			roofNum += 2 * i;
		}

		return roofNum;
	}

	private static int zigguratRoof(int l, int w)
	{
		int roofNum = l * w + (l - 2) * (w - 2);

		if (w % 2 != 0) //You're going to need to add slabs to the top of the roof if w is odd.
		{
			int stairs, slabs;
			slabs = (((l - 2) - (w - 2)) + 1); //Noticed a pattern.  That's what it is, every time.
			stairs = roofNum - 2 * slabs; //Start with the total number of block *spaces* for the roof and subtract the slabs.  Not yet ready to multiply by 1.5.  Multiply slabs by two since two stairs would otherwise hold one horizontal position.
			slabs /= 2; //Since a slab is half of a block, halve the value for slabs.
			stairs = (int) Math.round(stairs * 1.5); //You multiply by 1.5 *after* you've subtracted the requisite spaces for slabs.
			roofNum = slabs + stairs;
		}

		else
		{
			roofNum = (int) Math.round(roofNum * 1.5);
		}

		return roofNum;


	}

	private static int slantRoof(int l, int w)
	{
		int roofNum = 4 * l * w - 4 * l - 5 * w + 8;

		for (int i = w - 1; i > 0; i -= 1)
		{
			roofNum += 2 * i;
		}

		return roofNum;
	}

	private static int chooseRoof(BuildingSegment b, int select)
	{
		switch (select) 
		{
			case 1: System.out.println("The number of blocks necessary for a lengthwise slanted roof is " + slantRoof(b.length, b.width) + ".\n");
					return slantRoof(b.length, b.width);

			case 2: System.out.println("The number of blocks necessary for a widthwise slanted roof is " + slantRoof(b.width, b.length) + ".\n");
					return slantRoof(b.width, b.length);

			case 3: System.out.println("The number of blocks necessary for a lengthwise standard roof is " + standardRoof(b.length, b.width) + ".\n");
					return standardRoof(b.length, b.width);

			case 4: System.out.println("The number of blocks necessary for a widthwise standard roof is " + standardRoof(b.width, b.length) + ".\n");
					return standardRoof(b.width, b.length);

			case 5: System.out.println("The number of blocks necessary for a ziggurat roof is " + zigguratRoof(b.length, b.width) + ".\n");
					return zigguratRoof(b.length, b.width);

			default: 	System.out.println("The number of blocks necessary for a ziggurat roof is " + zigguratRoof(b.length, b.width) + ".\n");
						return zigguratRoof(b.length, b.width);
		}

	}

	private static int calculateBuildingSegment(BuildingSegment b)
	{
		return (((b.wallArea + b.floorArea) * b.storeys) + b.stairNum);
	}

	private static int calculateWholeBuilding(ArrayList<BuildingSegment> segs, int roofChoice)
	{
		int total = 0;

		for (int i = 0; i < segs.size(); i++)
		{
			total += calculateBuildingSegment(segs.get(i));

			if (i > 0 && segs.get(i - 1).floorArea > segs.get(i).floorArea) //Build an intermediate roof to avoid an open ceiling when the next segment is thinner.
			{
				total += segs.get(i - 1).floorArea - segs.get(i).floorArea;
			}

			if (i == segs.size() - 1)
			{
				total += chooseRoof(segs.get(i), roofChoice);
				total -= 3 * segs.get(i).height - 3.5; //Remove the stairs leading to the roof.  We need those like we need haystacks.

				if (segs.get(i).twoStaircases)
				{
					total -= 3 * segs.get(i).height - 3.5; //If twoStaircases is true, then there will be another extra set of stairs.  Delete that, too.
				}
			}
		}

		System.out.println("With this roof, the total number of necessary blocks will be " + Integer.toString(total) + ".\n");
		System.out.println("25% will probably be windows or doors, so if you want to account for that,\nthe total is around " + Long.toString(Math.round(total * 0.75)) + " blocks.\n");

		return total;
	}

	private static void fullEvaluation (ArrayList<BuildingSegment> building)
	{
		int widthOfTop = building.get(building.size() - 1).width, lengthOfTop = building.get(building.size() - 1).length;

		for(int i = 0; i < building.size(); i++)
		{
			if (i > 0 && building.get(i - 1).floorArea > building.get(i).floorArea) //Build an intermediate roof to avoid an open ceiling when the next segment is thinner.
			{
				System.out.println("An intermediate, simple roof of slabs is necessary between segments " + Integer.toString(i) + " and " + Integer.toString(i + 1) + ". This will cost " + Integer.toString(building.get(i - 1).floorArea - building.get(i).floorArea) + " blocks.\n");
			}
		}

		for (int i = 1; i < 6; i++)
		{
			calculateWholeBuilding(building, i);
		}

		System.out.println("NB: Since Minecraft houses cannot really be reduced to an algorithm, reaching the\ntheoretical number of blocks for a windowless, doorless house will allow you\nto make lots of embellishments along the way.\n");
	}

	private static void passParameters(ArrayList<BuildingSegment> building, Scanner s)
	{
		System.out.println("What's the length of this segment (anything below 6 resolves to 6)?\n");
		int lIn = Math.max(6, s.nextInt());
		System.out.println("\nAnd the width (anything below 6 resolves to 6)?\n");
		int wIn = Math.max(6, s.nextInt());
		System.out.println("\nAnd the height in blocks for EACH FLOOR in the segment (anything below 4 resolves to 4)?\n");
		int hIn = Math.max(4, s.nextInt());
		System.out.println("\nAlright.  And for how many storeys do you want the former 3 parameters to persist (anything below 1 resolves to 1)?\n");
		int sIn = Math.max(1, s.nextInt());
		System.out.println("\nTwo staircases (pass true or false)?\n");
		boolean tsIn = s.nextBoolean();
		building.add(new BuildingSegment(lIn, wIn, hIn, sIn, tsIn));
		System.out.println("This segment is complete. Do you want to make another one (pass true or false)?\n");
		boolean newSeg = s.nextBoolean();

		if (newSeg)
		{
			System.out.println("\nOnto the next segment...\n");
			passParameters(building, s);
		}

		else
		{
			s.close();
			System.out.println("\nVery well.  Here's a breakdown of what you'll need...\n");
			fullEvaluation(building);
		}
	}

	public static void main(String[] args)
	{
		System.out.println("\nLet's start with the first segment...\n");
		ArrayList<BuildingSegment> theBuilding = new ArrayList<BuildingSegment>();
		Scanner reader = new Scanner(System.in);
		passParameters(theBuilding, reader);
	}
}
