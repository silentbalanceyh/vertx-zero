# Utility X, thenParallel

This chapter we'll start to focus on advanced future usage, here we'll list some real project problems and consider how
to resolve this problem with Utility X tool in zero system.

## 1. Parallel Futures

We often meet some situations such as following:

1. We have some videos in our project, you want to search all the videos, either it's uploaded by you or other users;
2. When you get the video list, you want to know some additional status of each video.
3. Then in the video list, we need a field named "subscribed" to record whether you have subscribed this video.

In this situation, we have two tables: Video & Subscription, and we want to get the video list, but each record should
do the secondary query to check whether this video has been subscribed. Here are the Future for this kind of situation:

> This example is because in the project we used Mongo instead of SQL, there is no join statement and we need to mention
> that the api is not the best solution, but only introduce the api usage to tell you how to build the Future in zero.

```java
static <F, S, T> Future<List<T>> thenParallel(
    final Future<List<F>> source, 
    final Function<F, Future<S>> generateFun, 
    final BiFunction<F, S, T> mergeFun
)
```

The workflow for this api is as following:

1. Query database and returned `Future<List<F>>`, in our situation, the F means Video.
2. Provide function to generate another `Future<S>`, the function argument is the result of step 1, then this function
   will impact each element of List&lt;F&gt;.
3. When the secondary futures executed, it means that each F will generate `Future<S>` and get `S`, the last `mergeFun`
   will let you process `F, S` to `T` and returned `Future<List<T>>`

## 2. Explain Workflow

The workflow of this api is as following:

![](/doc/image/D10057-1.png)

## 3. Summary

This api is a little complex because it provide parallel operations on `List` and generate new `List`, in above workflow
the F is the video, we could use video to generate each subscription record that belong to yourself, then each element
will be executed to build the result `T`, The last result `List<T>` contains the video list with additional field "
subscribed" that tell you that whether you have subscribed the video. 



