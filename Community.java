import java.util.HashMap;

public class Community {

    private final HashMap<String, String> data;

    public Community(HashMap<String, String> data) {
        this.data = data;
    }

    public HashMap<String, String> getData() {
        return data;
    }

    String allCommunity() {
        String finalStr = "";
        String users = DataBase.getSingleTone().getController("Community").readFile();
        String[] split = users.split("\n");
        for (int i = 0; i < split.length; i++) {
            String[] s = split[i].split(";");
            finalStr += s[0];
            if (i + 1 != split.length) {
                finalStr += ";";
            }
        }
        return finalStr;
    }

    String createCommunity() {
        DataBase.getSingleTone().getController("Community").writeFile(data.get("communityName" + ";" + data.get("username") + ";" + "null" + ";" + "null" + "\n"));
        return "valid";
    }

    String removePost() {
        String post = DataBase.getSingleTone().getController("Community").readFile();
        String[] split = post.split("\n");
        int index = 0;
        for (int i = 0; i < split.length; i++) {
            String[] s = split[i].split(";");
            if (s[0].equals(data.get("postId"))) {
                index = Integer.parseInt(s[0]);
            }
        }
        String finalStr = "";
        for (int i = 0; i < split.length; i++) {
            if (i == index)
                continue;
            finalStr += split[i] + "\n";
        }
        DataBase.getSingleTone().getController("Community").writeFile(finalStr);
        return "valid";
    }

    String communityProfile() {
        String community = DataBase.getSingleTone().getController("Community").readFile();
        String[] split = community.split("\n");
        String finalStr = "";
        for (int i = 0; i < split.length; i++) {
            String s[] = split[i].split(";");
            if (data.get("communityName").equals(s[0])) {
                finalStr += "communityName: " + s[0] + "\n";
                finalStr += "mainAdmin: " + s[1] + "\n";
                finalStr += "admins: " + s[2] + "\n";
                finalStr += "info: " + s[3] + "\n";
            }
        }
        return finalStr;
    }

    String addAdmin() {
        String community = DataBase.getSingleTone().getController("Community").readFile();
        String[] split = community.split("\n");
        String finalStr = ";";
        for (int i = 0; i < split.length; i++) {
            String s[] = split[i].split(";");
            if (data.get("communityName").equals(s[0])) {
                if (s[2].equals("null")) {
                    s[2] = data.get("user");
                } else {
                    s[2] += "-" + data.get("user");
                }
                split[i] = s[0] + ";" + s[1] + ";" + s[2] + ";" + s[3] + "\n";
                break;
            }
        }

        for (int i = 0; i < split.length; i++) {
            finalStr += split[i] + "\n";
        }
        DataBase.getSingleTone().getController("Community").writeFile(finalStr);
        return "valid";
    }

    String editCommunity() {
        String community = DataBase.getSingleTone().getController("Community").readFile();
        String[] split = community.split("\n");
        String finalStr = ";";
        for (int i = 0; i < split.length; i++) {
            String s[] = split[i].split(";");
            if (data.get("communityName").equals(s[0])) {
                s[3] = data.get("info");
                split[i]  = s[0] + ";" + s[1] + ";" + s[2] + ";" + s[3] + "\n";
                break;
            }
        }
        for (int i = 0; i < split.length; i++) {
            finalStr += split[i] + "\n";
        }
        DataBase.getSingleTone().getController("Community").writeFile(finalStr);
        return "valid";

    }


}

