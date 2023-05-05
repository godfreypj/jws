import tips.TipServiceService;

import java.util.List;

import tips.TipServiceInterface;

public class TipsClientApp {
    public static void main(String[] args) {
        // setup
        TipServiceService service = new TipServiceService();
        TipServiceInterface port = service.getTipServicePort();

        // sample calls
        System.out.println(port.getTip());
        System.out.println();
        List<String> nums = port.getTips();
        for (String num : nums)
            System.out.println(num);
    }
}