# <center>目錄結構描述 </center>
```
Project                         //遊戲根目錄
│
├─ audio                        //遊戲音樂及音效
│  ├─ BGM.wav                   //音樂—遊戲進行
│  ├─ Button.wav                //音效—按鈕
│  ├─ ButtonRollOver.wav        //音效—按鈕懸停
│  ├─ GarbageTimeUp.wav         //音效—垃圾倒計時歸零
│  ├─ Place.wav                 //音效—放置垃圾
│  ├─ Ranking.wav               //音樂—排名畫面
│  ├─ SetPortal.wav             //音效—傳送門狀態轉換
│  ├─ Shield.wav                //音效—拾取盾牌
│  ├─ Title.wav                 //音樂—標題畫面
│  ├─ Transport.wav             //音效-傳送
│  └─ WhenPortalIsOpen.wav      //音效-傳送門開啟期間
│
├─ bin                          //程式檔案
│  ├─ AI.class                  //(繼承於Character)定義敵方AI如何行動
│  ├─ Audio$1.class             //anonymous LineListener，用以偵測音檔播放狀況
│  ├─ Audio.class               //音檔取得與控制
│  ├─ Character.class           //角色，定義角色狀態及行動，分為玩家可操控角色及敵方AI（繼承）
│  ├─ Game.class                //主程式
│  ├─ Garbage.class             //(繼承於Obj)垃圾
│  ├─ Icon.class                //圖像取得
│  ├─ Map$1.class               //anonymous class extends TimerTask，用於定時偵測地圖上的垃圾
│  ├─ Map$Task.class            //inner class extends TimerTask，用於不定時生成炸彈
│  ├─ Map.class                 //地圖，記錄、管理與繪製較易變動的物件
│  ├─ Map1.class                //實作地圖類型1
│  ├─ MapType.class             //(abstract)地圖類型模板，記錄會根據地圖類型而改變的物件
│  ├─ Obj.class                 //遊戲物件，包含垃圾（繼承）、家、傳送門、盾牌（繼承）
│  ├─ Shield.class              //(繼承於Obj)盾牌
│  ├─ Threads$1.class           //編譯時可能產生的空檔案，不影響遊玩。
│  ├─ Threads$AI_Thread.class   //inner class extends Thread，用以實現多名角色「同時行動」
│  ├─ Threads$ButtonListener    //inner class extends MouseAdapter，用於偵測按鈕、切換效果並播放對應音效
│  ├─ Threads$GamePanel.class   //inner class extends JPanel，用於繪製遊戲畫面
│  ├─ Threads$Keyboard.class    //inner class extends KeyAdapter，用於偵測按鍵及操作角色
│  ├─ Threads$Task.class        //inner class extends TimerTask，用以進行遊戲倒計時及傳送門狀態切換的判定
│  └─ Threads.class             //遊戲主執行緒，負責視窗及畫面之設定、遊戲的部分判定與管理以及其他執行緒的控制
│
├─ icon                         //圖像
│  ├─ Character1.png            //角色1（玩家）
│  ├─ Character2.png            //角色2（AI）
│  ├─ Character3.png            //角色3（AI）
│  ├─ Character4.png            //角色4（AI）
│  ├─ ExitButton.png            //「離開遊戲」按鈕（標題）
│  ├─ ExitButtonRollOver.png    //「離開遊戲」按鈕（懸停）（標題）
│  ├─ GameOver.png              //排名畫面
│  ├─ Garbage.png               //垃圾
│  ├─ GarbageIcon.png           //垃圾（狀態列）
│  ├─ Home1.png                 //家1
│  ├─ Home2.png                 //家2
│  ├─ Home3.png                 //家3
│  ├─ Home4.png                 //家4
│  ├─ Icon.png                  //程式圖示
│  ├─ Map1.png                  //實作地圖類型1之地圖背景
│  ├─ Next.png                  //「下一頁」按鈕（規則）
│  ├─ NoGarbageIcon.png         //無垃圾（狀態列）
│  ├─ OK.png                    //「OK」按鈕（規則）
│  ├─ Portal1.png               //紫色傳送門
│  ├─ Portal2.png               //藍色傳送門
│  ├─ Previous.png              //「上一頁」按鈕（規則）
│  ├─ Rule1.png                 //規則畫面第1頁
│  ├─ Rule2.png                 //規則畫面第2頁
│  ├─ RuleButton.png            //「規則說明」按鈕（標題）
│  ├─ RuleButtonRollOver.png    //「規則說明」按鈕（懸停）（標題）
│  ├─ Shield.png                //盾牌
│  ├─ StartButton.png           //「開始遊戲」按鈕（標題）
│  ├─ StartButtonRollOver.png   //「開始遊戲」按鈕（懸停）（標題）
│  ├─ StatusBar.png             //狀態列
│  └─ TitleScreen.png           //標題畫面
│
├─ src                          //原始碼
│  ├─ AI.java                   //(繼承於Character)定義敵方AI如何行動
│  ├─ Audio.java                //音檔取得與控制
│  ├─ Character.java            //角色，定義角色狀態及行動，分為玩家可操控角色及敵方AI（繼承）
│  ├─ Game.java                 //主程式
│  ├─ Garbage.java              //(繼承於Obj)垃圾
│  ├─ Icon.java                 //圖像取得
│  ├─ Map.java                  //地圖，記錄、管理與繪製較易變動的物件
│  ├─ Map1.java                 //實作地圖類型1
│  ├─ MapType.java              //(abstract)地圖類型模板，記錄會根據地圖類型而改變的物件
│  ├─ Obj.java                  //遊戲物件，包含垃圾（繼承）、家、傳送門、盾牌（繼承）
│  ├─ Shield.java               //(繼承於Obj)盾牌
│  └─ Threads.java              //遊戲主執行緒，負責視窗及畫面之設定、遊戲的部分判定與管理以及其他執行緒的控制
│
├─ readme.md                    //help
└─ 潔境.jar                     //可藉由執行該檔案直接執行並開啟遊戲
```
***
***
# <center>開發軟體工具安裝之順序步驟與設定</center>
本遊戲採用 Java 進行開發，想執行遊戲必須事先安裝 Java。

本遊戲使用 OpenJDK 1.8.0_292 進行編譯，過低的 Java 版本可能導致程式無法執行，請確保您的裝置在 Java 執行環境 （JRE）的版本不低於 8u292 。

若您不清楚如何選擇版本，我們建議您選用 Java 8 或更高的版本。

若您尚未安裝Java或安裝的版本較低導致無法執行，可以經由下列官方連結下載並根據指示進行安裝：

*https://www.oracle.com/java/technologies/downloads/*

或

*https://www.java.com/zh-TW/download/manual.jsp*
***
***
# <center>自行開發程式碼之安裝設定</center>
在「Project.zip」壓縮檔內有一個「Project」資料夾，內含遊戲文件。

對壓縮檔進行解壓縮後，請進入該資料夾。

您可以看到裡面含有四個子目錄「audio」、「bin」、「icon」、「src」，

以及兩個檔案「潔境.jar」、「readme.md」（即本份說明文件）。

基本上，解壓縮即是安裝完成。

    注意！檔案路徑不能有中文！否則會造成程式無法順利執行。
***
***
# <center>遊戲規則與操作說明</center>
- ### 遊戲規則如下：
	- 遊戲時間：一局 300 秒。
    - 遊玩人數：1 人。
        ***
            遊戲中共有 4 個角色， 1 個由玩家操控，其他為敵方 AI。
            每個角色都有其代表色，分別是：
        <center>「<font color=#B7B6B5>灰色</font>」、「<font color=##A5B2CE>淺藍</font>」、「<font color=orange>橘色</font>」、「<font color=#D5ABA8>粉色</font>」</center>
        　

        每個角色都有自己的地盤及家，地圖上的不同顏色即對應各角色地盤。
        
        此外，（較深的）藍色為公共區域，白色則為障礙物。
        ***
        角色初始各有 10 點生活品質（可理解為角色血量）。

        當生活品質清零時，該角色出局，其地盤將被視為公共區域。
        ***
        每 5 ~ 7 秒，地圖上會隨機生成垃圾。
        
        若是垃圾留在地上達到 5 秒，就會根據垃圾所在位置進行生活品質扣除判定。
        - 位於<b>「公共區域」</b>　或　<b>「已出局的角色地盤」</b>
            
            「全體存活角色」扣除「 1 」點生活品質。
            ***
        - 位於「仍存活的角色地盤」
            - 位於「以該角色的家為中心的九宮格內」

                「該角色」扣除「 2 」點生活品質。
                ***
            - 位於「其他位置」

                「該角色」扣除「 1 」點生活品質。

        若拾取地圖上的盾牌則可以回復3 點生活品質。
        ***
        角色地盤各有一個傳送門，會在「開啟」及「關閉」兩種狀態中循環，每種狀態皆持續 10 秒。

        當傳送門開啟時，同樣顏色的傳送門間可以互通。
        ***
        各角色最多能同時拿著 5 個垃圾，若未拿著任何垃圾則無法放置垃圾。
        ***

     - 如何結束：當遊戲時間歸零或只剩一名角色存活，則遊戲結束並列出排名。
 - 遊戲操作方式：
     - 方向鍵／ WASD 鍵：移動。
     - 空格鍵／ Enter 鍵：放置或撿起垃圾。
     <center><font color = red>若是 WASD 鍵及空白鍵無反應，請確認鍵盤是否已切換為英數模式。</center>
     <center>若是在英數模式下依然無反應，則您的鍵盤可能出了一點問題:(</font></center>
***
***
# <center>如何測試安裝成功之步驟</center>
您可以透過以下幾種方式測試遊戲是否安裝成功：
- windows
 1. 雙擊「潔境.jar」來開啟遊戲。
 2. 按下 Windows 鍵 + R ，輸入"cmd"以開啟 cmd.exe，或是在開始選單中找到「Windows 系統」中的「命令提示字元」，並移動到遊戲根目錄下（.../Project/)　　　<b><font color = red>注意：目錄僅接受半形的英文數字</font></b>
    1. 透過輸入  <b><i>"java -jar 潔境.jar"</i></b>  開啟遊戲
    2. 透過輸入  <b><i>"java -cp ./bin Game"</i></b>  開啟遊戲
- linux

    解壓縮後進入遊戲根目錄 (.../Project/)

    1. 透過輸入  <b><i>"java -jar 潔境.jar"</i></b>  開啟遊戲
    2. 透過輸入  <b><i>"java -cp ./bin Game"</i></b>  開啟遊戲
    
***
要想完整顯示遊戲畫面，您的畫面可能需要有至少 寬 850 x 長 830 像素的螢幕解析度。

基於視窗外框考量，我們建議您的畫面至少要有 寬 855 x 長 860 像素的螢幕解析度，否則可能會有部分畫面顯示不全的問題。
***
若因 Java 版本問題而有重新編譯的需求，您可以透過在遊戲根目錄(.../Project/)下輸入指令 <b><i>"javac -encoding utf-8 -d ./bin ./src/*.java"</i></b> 來編譯。
***
### <center>若無法開啟遊戲，請先確認目錄皆為半形英文數字，以及是否已經安裝 Java。</center>
### <center>若以上皆確認過仍無法開啟遊戲，可能是您的 Java 版本過低，可以嘗試到官網下載當前的新版本</center>
***
