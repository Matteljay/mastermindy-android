package mmcore;

import java.util.ArrayList;

public class CodeProcessor {
	private ArrayList<Integer> secretCopy;
	private ArrayList<Integer> feelerCopy;
	private final HintStruct hint = new HintStruct();

	public ArrayList<Integer> newRandomSecret(int pawnSlots, int pawnAssort, boolean allowPawnCopies) {
		ArrayList<Integer> newSecret = new ArrayList<>();
		if(pawnSlots > pawnAssort) {
			allowPawnCopies = true;
		}
		while(newSecret.size() < pawnSlots) {
			int newPawn = (int)(Math.random() * pawnAssort);
			if(!allowPawnCopies && newSecret.contains(newPawn)) {
				continue;
			}
			newSecret.add(newPawn);
		}
		return newSecret;
	}

	public Boolean gameWon(ArrayList<Integer> theSecret, HintStruct hint) {
		return (hint.numBlacks >= theSecret.size());
	}

	public HintStruct computeHint(ArrayList<Integer> theSecret, ArrayList<Integer> feeler) {
		hint.numBlacks = hint.numWhites = 0;
		secretCopy = new ArrayList<>(theSecret);
		feelerCopy = new ArrayList<>(feeler);
		crossOutBlacks();
		crossOutWhites();
		hint.numBlanks = theSecret.size() - hint.numWhites - hint.numBlacks;
		return hint;
	}

	private void crossOutBlacks() {
		for(int i = 0; i < feelerCopy.size(); i++) {
			if(i < secretCopy.size() && feelerCopy.get(i).equals(secretCopy.get(i))) {
				feelerCopy.set(i, -1);
				secretCopy.set(i, -1);
				hint.numBlacks++;
			}
		}
	}

	private void crossOutWhites() {
		for(int i = 0; i < feelerCopy.size(); i++) {
			if(feelerCopy.get(i) == -1) {
				continue;
			}
			int feelInSec = secretCopy.indexOf(feelerCopy.get(i));
			if(feelInSec >= 0) {
				feelerCopy.set(i, -1);
				secretCopy.set(feelInSec, -1);
				hint.numWhites++;
			}
		}
	}
}
