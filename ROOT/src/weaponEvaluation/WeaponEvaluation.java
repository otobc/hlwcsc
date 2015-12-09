package weaponEvaluation;

import org.codehaus.jackson.map.ObjectMapper;
import java.io.IOException;

public class WeaponEvaluation {
	public static String weaponEvaluation(WeaponType weaponType, String json) {
		ObjectMapper mapper = new ObjectMapper();
		String resultInJson = new String();

		switch (weaponType) {
		case DDos:
			DDosEvaluation ddosEvaluation = new DDosEvaluation();
			try {
				ddosEvaluation = mapper.readValue(json, DDosEvaluation.class);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ddosEvaluation.ddosEvaluation();
			try {
				resultInJson = mapper.writeValueAsString(ddosEvaluation);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case Trojan:
			TrojanEvaluation trojanEvaluation = new TrojanEvaluation();
			try {
				trojanEvaluation = mapper.readValue(json, TrojanEvaluation.class);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			trojanEvaluation.trojanEvaluation();
			try {
				resultInJson = mapper.writeValueAsString(trojanEvaluation);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case Worm:
			WormEvaluation wormEvaluation = new WormEvaluation();
			try {
				wormEvaluation = mapper.readValue(json, WormEvaluation.class);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			wormEvaluation.wormEvaluation();
			try {
				resultInJson = mapper.writeValueAsString(wormEvaluation);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
		return resultInJson;
	}
}
