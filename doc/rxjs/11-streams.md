# Basic Programming

## 1. Blocking/Non-Blocking

```js
let items  = blockingHttpCall('/data');
items.forEach(item => {
    // process each items
});
```

![](/doc/rxjs/image/RX1-1-001.png)

```js
ajax('/data',
    items => {
        items.forEach(item => {
            // process each item
        });
    }
});
beginUiRendering();                // Called immediately after Ajax
```

![](/doc/rxjs/image/RX1-1-002.png)

