package mmcore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import androidx.annotation.NonNull;

public class StartupGenerator implements Iterable<ArrayList<Integer>> {
	private final int assortPawns;
	private final int numPawns;
	private final ArrayList<Integer> avoidSecret;
	private final ArrayList<Integer> feeler;
	private int iteratorCount;
	private boolean isRemainderProcessed;
	
	public StartupGenerator(int assortPawns, ArrayList<Integer> theSecret) {
		this.assortPawns = assortPawns;
		this.avoidSecret = theSecret;
		this.numPawns = theSecret.size();
		feeler = new ArrayList<>();
		iteratorCount = 0;
		isRemainderProcessed = false;
	}

	@Override
	public @NonNull
	Iterator<ArrayList<Integer>> iterator() {
		return new Iterator<ArrayList<Integer>>() {
			@Override
			public boolean hasNext() {
				if(assortPawns <= 1 || numPawns <= 1) {
					return false;
				}
				if(iteratorCount < assortPawns / numPawns) {
					return true;
				}
				if(!isRemainderProcessed && assortPawns % numPawns != 0) {
					isRemainderProcessed = true;
					return true;
				}
				return false;
			}
			@Override
			public ArrayList<Integer> next() {
				feeler.clear();
				if(isRemainderProcessed) {
					processRemainder();
				} else {
					copyRegularChunk();
				}
				iteratorCount++;
				shuffleIfMatchedSecret();
				return feeler;
			}
		};
	}
	
	private void processRemainder() {
		for(int i = assortPawns - 1; feeler.size() < numPawns; i--) {
			if(i < 0) {
				i = assortPawns - 1;
			}
			feeler.add(i);
		}
	}
	
	private void copyRegularChunk() {
		for(int i = 0; i < numPawns; i++) {
			feeler.add((numPawns * iteratorCount) + i);
		}
	}
	
	private void shuffleIfMatchedSecret() {
		for(int n = 0; feeler.equals(avoidSecret) && n < 10; n++) {
			Collections.shuffle(feeler);
		}
	}
}
