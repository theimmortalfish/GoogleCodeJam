package codejam.yr2013.qualification;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

// Treasure
// https://code.google.com/codejam/contest/2270488/dashboard#s=p3

public class Treasure {
	static BufferedWriter writer;
	static BufferedReader reader;

	public static void main(String[] args) throws IOException {

		File inFile = new File("test.in");
		File outFile = new File("test.out");
		StringBuilder solution = new StringBuilder();

		FileWriter fwriter = new FileWriter(outFile);
		writer = new BufferedWriter(fwriter);
		FileReader freader = new FileReader(inFile);
		reader = new BufferedReader(freader);

		int numCases = 0;
		numCases = Integer.parseInt(reader.readLine());
		for (int ca = 0; ca < numCases; ca++) {
			String[] kn = reader.readLine().split(" ");
			int k = Integer.parseInt(kn[0]);
			int n = Integer.parseInt(kn[1]);
			int[] keysIhave = new int[k];
			String[] inputKeyList = reader.readLine().split(" ");
			for (int a = 0; a < k; a++) {
				keysIhave[a] = Integer.parseInt(inputKeyList[a]);
			}
			Chest[] nChests = new Chest[n];
			for (int b = 0; b < n; b++) {
				String[] oneChest = reader.readLine().split(" ");
				int[] keys = new int[Integer.parseInt(oneChest[1])];
				for (int c = 0; c < keys.length; c++) {
					keys[c] = Integer.parseInt(oneChest[c + 2]);
				}
				nChests[b] = new Chest(b + 1, Integer.parseInt(oneChest[0]),
						keys);
			}
			String oneSolution = doThisCase(ca, nChests, keysIhave);
			solution.append(oneSolution + "\n");
		}
		writer.write(solution.toString());
		writer.close();
	}

	static class Chest {
		int chestNumber;
		int password; // use what key to open this chest
		int[] freeKeys; // the key given to you after the chest open

		public Chest(int a, int b, int[] c) {
			this.chestNumber = a;
			this.password = b;
			this.freeKeys = c;
		}
	}

	private static String doThisCase(int index, Chest[] chests, int[] keys) {
		String ans = open(chests, keys, "");
		if (ans.equals(""))
			return "Case #" + ++index + ": IMPOSSIBLE";
		else
			return "Case #" + ++index + ": " + ans;
	}

	private static String open(Chest[] chests, int[] keys, String currentMethod) {
		if (chests.length == 0) {
			return currentMethod.trim();
		}
		if (keys.length == 0) {
			return "";
		}
		Arrays.sort(keys);
		for (Chest ch : chests) {
			System.out.print(ch.chestNumber + " ");
		}
		for (int key : keys) {
			System.out.print(key + " ");
		}
		int pre = -1;
		for (int a = 0; a < keys.length; a++) {
			if (keys[a] == pre)
				continue;
			pre = keys[a];
			Chest[] newChests = null;
			boolean opened = false;
			int[] newKeys;
			// first find the chest that can be opened with this key
			for (int b = 0; b < chests.length; b++) {
				if (chests[b].password == keys[a]) {
					// add all its freeKeys to the newKeyList
					opened = true;
					newChests = new Chest[chests.length - 1];
					for (int c = 0; c < newChests.length; c++) {
						if (c < b)
							newChests[c] = chests[c];
						else
							newChests[c] = chests[c + 1];
					}
					Chest removedChest = chests[b];
					newKeys = new int[keys.length - 1
							+ removedChest.freeKeys.length];
					for (int d = 0; d < newKeys.length; d++) {
						if (d < a)
							newKeys[d] = keys[d];
						else if (d < keys.length - 1)
							newKeys[d] = keys[d + 1];
						else
							newKeys[d] = removedChest.freeKeys[d - keys.length
									+ 1];
					}
					String ansForUsingKeyA = open(newChests, newKeys,
							currentMethod + removedChest.chestNumber + " ");
					// need change!
					if (!ansForUsingKeyA.equals(""))
						return ansForUsingKeyA;
				}
			}
			if (!opened) {
				newKeys = new int[keys.length - 1];
				for (int d = 0; d < newKeys.length; d++) {
					if (d < a)
						newKeys[d] = keys[d];
					else if (d < keys.length - 1)
						newKeys[d] = keys[d + 1];
				}
				String ansForUsingKeyA = open(chests, newKeys, currentMethod);
				if (!ansForUsingKeyA.equals(""))
					return ansForUsingKeyA;
			}
		}
		// after all keys are being tried, and still at this point, no answer
		return "";
	}
}