package homework.hw5;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;

public class ProdConWithActors {
	
	public static class Request {}
	public static class Empty {}
	public static class StartOfProgram {}
	public static class EndOfProgram {}
	
	public volatile static int producerCount;
	public volatile static int consumerCount;
	
	public ProdConWithActors(int numProducers, int numConsumers){
		producerCount = numProducers;
		consumerCount = numConsumers;
	}
	
	public static void main(String[] args) {
		ProdConWithActors pcwa1 = new ProdConWithActors(5, 2);
		ProdConWithActors pcwa2 = new ProdConWithActors(2, 5);
		pcwa1.test();
	}
	
	public void test(){
		ActorSystem sys = ActorSystem.create("test-sys");
		ActorRef bufferRef = sys.actorOf(Props.create(BufferActor.class), "bufferActor");
		//ActorRef producerRef = sys.actorOf(ProducerActor.props("producerActor"));
		//ActorRef consumerRef = sys.actorOf(ConsumerActor.props("consumerActor"));
		bufferRef.tell(new StartOfProgram(), ActorRef.noSender());
		
	}
	
	public static class BufferActor extends AbstractActor{
		private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
		private final Queue<Integer> queue = new LinkedList<Integer>();
		private final int CAPACITY = 10;
		private ActorRef producerChild, consumerChild;
		
		public BufferActor(){
			producerChild = getContext().actorOf(Props.create(ProducerActor.class));
			consumerChild = getContext().actorOf(Props.create(ConsumerActor.class));
			getContext().watch(producerChild);
			getContext().watch(consumerChild);
		}
		
		//private final ActorRef producerChild = getContext().actorOf(ProducerActor.props("producerChild"));
		//private final ActorRef consumerChild = getContext().actorOf(ConsumerActor.props("consumerChild"));
		
		public static Props props(String text){
			return Props.create(BufferActor.class, text);
		}
		
		@Override
		public Receive createReceive() {
			ReceiveBuilder builder = ReceiveBuilder.create();
			
			builder.match(Request.class, this::process)
			.match(StartOfProgram.class, this::onProgramStart)
			.match(Integer.class, this::take)
			/*.match(Terminated.class, this::onChildTerminated)*/
			.match(EndOfProgram.class, this::onEndProgram)
			.matchAny(o -> log.info("received unknown message"));
			
			return builder.build();
		}
		
		private void onProgramStart(StartOfProgram sop){
			producerChild.tell(new Request(), getSelf());
			consumerChild.tell(new StartOfProgram(), getSelf());
		}
		
		private void onEndProgram(EndOfProgram eop){
			if(queue.isEmpty())
				getContext().stop(getSelf());
			else
				getSelf().tell(eop, getSelf());
		}
		
		/*private void onChildTerminated(Terminated t){
			ActorRef termAct = t.actor();
			if(termAct.equals(producerChild)){
				producerCount -= 1;
				if(producerCount > 0){
					producerChild = getContext().actorOf(ProducerActor.props("PRODUCER-" + producerCount));
					getContext().watch(producerChild);
				}
			}
			
		}*/
		
		public void request(ActorRef target){
			if(queue.size() < CAPACITY){
				log.info("Requesting produce from " + target.toString());
				target.tell(Request.class, getSelf());
			}
		}
		
		/*public void request(){
			request(producerChild);
		}*/
		
		private void process(Request r){
			if(queue.isEmpty()){
				request(producerChild);
				getSender().tell(new Empty(), getSelf());
			}
			else
				give(r);
		}
		
		public void give(Request request){
			log.info(getSender().path() + " sent request: sending item.");
			Integer item = queue.poll();
			if(item != null)
				getSender().tell(item, getSelf());
		}
		
		public void take(Integer item){
			log.info("Receiving item from " + getSender());
			queue.add(item);
		}
		
	}
	
	public static class ProducerActor extends AbstractActor{
		private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
		private final Random rand;
		private int count;
		private final ActorRef master;
		private static volatile int currentProducers = 0;
		
		public ProducerActor(){
			rand = new Random();
			master = getContext().getParent();
			count = 100;
			currentProducers += 1;
		}
		
		private void initialize(StartOfProgram sop){
			final int childrenToHave = producerCount - 1;
			for(int i = 0; i < childrenToHave; i++){
				getContext().actorOf(ConsumerActor.props("PRODUCER-" + i));
			}
		}
		
		@Override
		public Receive createReceive() {
			ReceiveBuilder builder = new ReceiveBuilder();
			
			builder.match(Request.class, this::process)
			.match(StartOfProgram.class, this::initialize)
			.matchAny(o -> log.info("received unknown message"));
			
			return builder.build();
		}
		
		public void process(Request s){
			log.info("Received request from " + getSender().path());
			if(count <= 0)
			{
				log.info("I'm all done");
				//getSelf().tell(Stop.class, getSender());
				getContext().stop(getSelf());
			}else{
				count -= 1;
				log.info("Sending item to buffer: " + getSender());
				getSender().tell(produce(), getSelf());
			}
		}
		
		private Integer produce(){
			return rand.nextInt();
		}
		
		static Props props(String name){
			return Props.create(ProducerActor.class);
		}
		
	}
	
	public static class ConsumerActor extends AbstractActor{
		private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
		private final ActorRef master = context().parent();
		private volatile boolean ready = true;
				
		@Override
		public Receive createReceive() {
			ReceiveBuilder builder = new ReceiveBuilder();
			
			builder.match(Integer.class, this::process)
			.match(StartOfProgram.class, this::initialize)
			.match(Empty.class, this::waitForMore)
			.matchAny(o -> log.info("Received unknown message"));
			
			return builder.build();
		}
		
		private void initialize(StartOfProgram sop){
			final int childrenToHave = consumerCount - 1;
			for(int i = 0; i < childrenToHave; i++){
				getContext().actorOf(ConsumerActor.props("CONSUMER-" + i)).tell(new Empty(), getContext().getParent());;
			}
			
		}
		
		private void waitForMore(Empty emp){
			ready = false;
			log.info("Received Empty message: waiting 1 sec");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}finally{
				ready = true;
			}
		}
		
		private void process(Integer item){
			ready = false;
			log.info("Removed from buffer: {}", item);
			ready = true;
		}
		
		private void ready(ActorRef target){
			if(ready)
				target.tell(new Request(), getSelf());
		}
		
		private void ready(){
			ready(master);
		}
		
		static Props props(String name){
			return Props.create(ConsumerActor.class, name);
		}
		
	}
}
