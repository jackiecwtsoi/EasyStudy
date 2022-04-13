### Database APi Reference

1. 把有假数据的数据库放进device file explorer中 data->data->com.example.learning-> databases中上传以下三个文件并覆盖，这三个文件就在github上，和这个readme同目录下

   ![image-20220413234937877](/Users/luchixiang/Library/Application Support/typora-user-images/image-20220413234937877.png)

2. 关于如何使用接口

   1. 在fragment初始化的时候传入一个SQLiteDatabase 对象（在mainactivity）中传入，并且已经给你们改好了，之后如果有自己额外的fragment,把这个参数传递过去就好了

   ![image-20220413235244820](/Users/luchixiang/Library/Application Support/typora-user-images/image-20220413235244820.png)

		2. 有三个实体类，分别为FolderEntity, DeckEntity, Card，里面分别存有我们需要的属性，比如folder 有folderName, folderDescription, time, id.其他实体的属性可以看代码，然后可以通过get方法获取，比如getDescription就会返回该folder的description![image-20220413235557987](/Users/luchixiang/Library/Application Support/typora-user-images/image-20220413235557987.png)3. DBApi类： 每次要调用数据库的时候，new一个这个类，传入之前的SQLiteDatabase 对象。然后就可以用这个接口的方法

​	![image-20220413235745016](/Users/luchixiang/Library/Application Support/typora-user-images/image-20220413235745016.png)

目前该接口有的方法

- queryFolder(int userID): 给userId,返回一个 FolderEntity 的ArrayList， list中的每一个元素代表了一个第二点中FolderEntity的对象，可以用for循环遍历然后通过比如getName或者每一个Folder的名字
- queryDeck(int userID, int folderID)  给userId 和folderID, 返回一个 DeckEntity 的ArrayList， list中的每一个元素代表了一个第二点中DeckEntity的对象，可以用for循环遍历然后通过比如getName或者每一个Deck的名字
- queryCard(int userID, int deckID)给userId 和deckID, 返回一个 Card 的ArrayList， list中的每一个元素代表了一个第二点中Card的对象，可以用for循环遍历然后通过比如getQuestion或者每一个Card的问题
- insertFolder(String folderName, String folderDescription, int userID) 像数据库中插入一条folder，要求给名字，描述，userid
- insertDeck(String deckName, String deckDescription, int completion, int folderID, int userID)像数据库中插入一条deck，要求给名字，描述，userid, 完成度，folderid
- insertCard(String cardName, String cardQuestion, String cardAnswer, int hardness, int deckID, int userID) 像数据库中插入一条card，要求给问题，答案，userid, 难度，deckid



目前只有插入和查询功能，如果有需要其他功能或者其他属性，可以联系我。

然后目前的数据库中有3个user， userid 分别是1，2，3，name分别为Mike, Jasper, Amy, 每个user有三个folder, 每个folder有三个deck,每个deck有三个问题。

