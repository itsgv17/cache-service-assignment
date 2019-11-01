# cache-service-assignment
Generic cache server 

Used Spring boot and Concurrent map datastructure(having two levels) for implementation.
 ```
ConcurrentMap<String, Map<String, CacheObject>> twoLevelMap = ConcurrentHashMap<>();

Level 1 Map  uses collections as keys and Map( having cacheKey and Cache Values) as Values.

Level2 Map( result value of Level 1 Map)-> contains actual cache key and values.

e.g.Map<String, CacheObject> levelOneMap= twoLevelMap.get("CollectionKey");


Two Level Map data Structure example:

{
  "Registration": {
    "/{id=1}/register": "{name:gobinath,pass:@#845HJHj}",
    "/{id=2}/register": "{name:rama, pass:&*yyFH^8}"
  },
  "Payment": {
    "/{id=2}/pay": "{fromId:123, toId:235, amount:100}",
    "/{id=2}/credit": "{id:123, amount:100}",
    "/{id=3}/debit": "{id=1345, amount:200}"
  }
}

InOrder to fetch cached data from CacheServer, client has to send collection key as well as actual key.

Above approach will partition cache data based on collectionKey,
so the search will only happen on level2 Map which is the result of twoLevelMap.get("CollectionKey");

```
Implemented units for Controller and service layers.


Created three rest endpoints for Cache operation.
  - Fetching cached data
  ```
  OkHttpClient client = new OkHttpClient();

   Request request = new Request.Builder()
  .url("http://localhost:8080/cache/v1/get")
  .get()
  .addHeader("Content-Type", "application/json")
  .addHeader("collection", "Registration")
  .addHeader("key", "/windmill/manager/v1/register")
  .addHeader("Accept", "*/*")
  .addHeader("Cache-Control", "no-cache")
  .addHeader("Host", "localhost:8080")
  .addHeader("Accept-Encoding", "gzip, deflate")
  .addHeader("Connection", "keep-alive")
  .addHeader("cache-control", "no-cache")
  .build();

  Response response = client.newCall(request).execute();'
  
  ```
  - Posting data to cache server
  ```
  
  OkHttpClient client = new OkHttpClient();

MediaType mediaType = MediaType.parse("application/json");
RequestBody body = RequestBody.create(mediaType, "{\n    \"serialNumber\": \"1234567890qwerty\",\n    \"latitude\": 123.001,\n    \"longitude\": \"123.002\"\n}");
Request request = new Request.Builder()
  .url("http://localhost:8080/cache/v1/put")
  .put(body)
  .addHeader("Content-Type", "application/json")
  .addHeader("collection", "Registration")
  .addHeader("key", "/windmill/manager/v1/register")
  .addHeader("Accept", "*/*")
  .addHeader("Cache-Control", "no-cache")
  .addHeader("Host", "localhost:8080")
  .addHeader("Accept-Encoding", "gzip, deflate")
  .addHeader("Content-Length", "95")
  .addHeader("Connection", "keep-alive")
  .addHeader("cache-control", "no-cache")
  .build();

Response response = client.newCall(request).execute();

  ```
  - Deleting cache from server
  ```
  OkHttpClient client = new OkHttpClient();

Request request = new Request.Builder()
  .url("http://localhost:8080/cache/v1/delete")
  .delete(null)
  .addHeader("Content-Type", "application/json")
  .addHeader("collection", "Registration")
  .addHeader("key", "/windmill/manager/v1/register")
  .addHeader("Accept", "*/*")
  .addHeader("Cache-Control", "no-cache")
  .addHeader("Host", "localhost:8080")
  .addHeader("Accept-Encoding", "gzip, deflate")
  .addHeader("Content-Length", "0")
  .addHeader("Connection", "keep-alive")
  .addHeader("cache-control", "no-cache")
  .build();

Response response = client.newCall(request).execute();

  ```
  - Cache Eviction Scheadular
  ```
  
  It runs every one minute, removes expired cache objects
  
  Implemented approach iterates entire data structure & evicts expired cached object.
  
 Can impove performance by maintaining SkipListDS(Singly Linked list with multiple level),ref->https://en.wikipedia.org/wiki/Skip_list
 
 Higher level LL maintains time ranges , first and last node reference in lower level linked list which falls within that defined time range.
 
 when you put elements in cache , second LL appends cache elements along with collectionKey and CacheKey in Second LL.
 
 When there is a cache eviction, Scheadular first  searches the higher level linked list , finds node ranges eligible for eviction.
 Then Hihger Level LL uses Second LL ref. to remove expired Cache objects in Constant time O(1).
 
 Finally need to remove unsed nodes in Skip list too.
 
  ```
  
  
  
  
  
