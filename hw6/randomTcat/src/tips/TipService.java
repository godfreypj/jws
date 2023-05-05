package tips;

import javax.jws.WebService;
import javax.jws.WebMethod;
import java.util.Random;

@WebService
public class TipService {
    private static final String[] tips = {
            "Always be kind to others.",
            "Take breaks often to rest your mind.",
            "Drink plenty of water to stay hydrated.",
            "Exercise regularly to stay healthy.",
            "Set achievable goals and work towards them."
    };

    @WebMethod
    public String getTip() {
        int randomIndex = new Random().nextInt(tips.length);
        return tips[randomIndex];
    }

    @WebMethod
    public String[] getTips() {
        return tips;
    }
}
