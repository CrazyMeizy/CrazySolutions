# Disclaimer

В домашних заданиях вы можете менять сигнатуры методов/интерфейс, если не написано обратного. Если в задании нужно
реализовать функцию, которая уже объявлена за вас, то в таких случаях ее сигнатуру менять нельзя. То же
касается и интерфейсов/адт: если задана четкая структура с полями трейтов/классов, то ее можно только дополнять для
удобства вашего решения, но не менять кардинально.

## Важно: автоматические 0 баллов за работу, если:
* Сдано после дедлайна
* Красный CI (даже если падает только `scalafmt`)
* Если CI проходит, но было выполнено 0 тестов
* Есть правки в условии, которые не разрешены в условии
* Если есть действия после дедлайна, которые меняют код 

# Type Classes Hierarchy

Ваша задача - реализовать иерархию тайпклассов, данную в `task1.hierarchy.Typeclasses.scala`
для простого бинарного дерева, данного в [`Tree.scala`](./src/main/scala/task1/Tree.scala).

В этом задании вам не нужно писать тесты, только написать экземпляры (инстансы) тайпклассов, определенных в файле [TypeClasses.scala](src/main/scala/task1/hierarchy/TypeClasses.scala). За изменения в тестах могут сниматься баллы. Если без изменений в тестах все-таки никак, обращайтесь к ассистентам в Telegram-чате.

# Работа с Eval

С помощью тайпкласса [Eval](https://typelevel.org/cats/datatypes/eval.html), напишите стекобезопасно:

1) Функцию, вычисляющую `n-ный` член последовательности Фибоначчи (0 - 0, 1 - 1, 2 - 1...):
   ```scala
    def fib(n: Int): Eval[BigInt] = ???
   ```
2) Функцию foldRight:
    ```scala
    def foldRight[A, B](as: List[A], acc: Eval[B])(fn: (A, Eval[B]) => Eval[B]): Eval[B] = ???
    ```

Так же как и в 1-ом задании, тут не нужно реализовывать тесты, достаточно будет реализовать функции в файле [EvalTricks.scala](src/main/scala/task2/EvalTricks.scala). За изменения в тестах могут сниматься баллы. Если без изменений в тестах все-таки никак, обращайтесь к ассистентам в Telegram-чате. 

### Code Style:

Мы последовательно вводим список запрещенных механик, которыми нельзя пользоваться при написании кода, и рекомендаций по
code style. За нарушения мы оставляем за собой право **снижать оценку**.

* Переменные и функции должны иметь осмысленные названия;
* Тест классы именуются `<ClassName>Spec`, где `<ClassName>` - класс к которому пишутся тесты;
* Тест классы находятся в том же пакете, что и класс к которому пишутся тесты (например, класс `Fibonacci` находится в
  пакете `fibonacci` в директории `src/main/scala/fibonacci`, значит его тест класс `FibonacciSpec` должен быть в том же
  пакете в директории `src/test/scala/fibonacci`);
* Каждый тест должен быть в отдельном test suite;
* Использовать java коллекции запрещается (Используйте `Scala` коллекции);
* Использовать `mutable` коллекции запрещается;
* Использовать `var` запрещается;
* Использование `this` запрещается (используйте `self` если требуется);
* Использование `return` запрещается;
* Использование `System.exit` запрещается;
* Касты или проверки на типы с помощью методов из Java вроде `asInstanceOf` запрещаются;
* Использовать `throw` запрещено;
* Использование циклов запрещается (используйте `for comprehension`, `tailRec`, методы `Monad`, `fold`);
* Использование не безопасных вызовов разрешено только в тестах (например `.get` у `Option`);
* Использование взятия и освобождения примитивов синхронизации: semaphore, mutex - из разных потоков запрещено;
* Использование require для ошибок запрещается;
