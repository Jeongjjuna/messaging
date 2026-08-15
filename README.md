# Nats



---

# Kafak

### 카프카 서버 토픽 생성
```shell
/opt/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --create --topic event.completed
```

### 카프카 서버 토픽 목록 확인
```shell
/opt/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --list
```

### 카프카 서버 토픽 세부정보 조회
```shell
/opt/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --describe --topic event.completed

#  결과
Topic: event.completed	TopicId: QpqCRsrESp2tfGctQ-RckQ	PartitionCount: 4	ReplicationFactor: 1	Configs:
	Topic: event.completed	Partition: 0	Leader: 1	Replicas: 1	Isr: 1
	Topic: event.completed	Partition: 1	Leader: 1	Replicas: 1	Isr: 1
	Topic: event.completed	Partition: 2	Leader: 1	Replicas: 1	Isr: 1
	Topic: event.completed	Partition: 3	Leader: 1	Replicas: 1	Isr: 1
```

### 토픽 삭제하기
```shell
/opt/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --delete --topic event.completed
```
