Как это должно бы работать.

Внутри репозитория установлены все зависимости, нужные для правильной сборки сети
Устанавливалось командой


Из директории fabcar

./startFabric.sh java - развертывание сети (под капотом вызывается скрипт из test-network скриптом network.sh)
После этого можно уже работать с приложением. Для этого в директории java собирается проект с помощью mvn test.
Я когда тестила, запускала прямо из IDE - соотетсвенно EnrollAdmin работал, RegisterUser работал (в wallet2 (для второй организации даже ключи сохранились));
ClientApp - позволяет исполнять chaincode.
Смарт-контракты которые были собраны gradle лежат по пути /chaincode/fabcar/java/build/classes/java/main/org/hyperledger/fabric/samples/oddeven/
Исходники - /chaincode/fabcar/java/src/main/java/org/hyperledger/fabric/samples/oddeven/
С проверкой на четность немного неверно реализовано, как мне кажется - но логика у меня была такая - что прямо в методе проверки числа, вытянуть его по query и проверить его и сделать транзакцию уже по четности-нечетности.
В итоге смарт-контракты я не разделила между организациями.
А вот сам код приложения, я просто в целях упрощения сделала отдельными классами для организаций 1 и 2 - это исключительно для наглядности (по хорошему, для более правильной структуры я бы вызывала EnrollAdmin с аргументом, например org1 или org2), и в зависимости от переданного аргумента - регистрировалася бы админ для нужной организации).

Тот скрипт, что есть, неверно регистрирует chaincode, и сборка jar файла у меня для исходного примера.

В итоге - вот такая недоделка.
В любом случае, для себя доделаю, планирую сделать такую маленькую тестовую площадку, подобавляю других контрактов. 