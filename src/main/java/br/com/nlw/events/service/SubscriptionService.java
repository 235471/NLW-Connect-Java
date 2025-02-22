package br.com.nlw.events.service;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.nlw.events.dto.SubscriptionRankingByUser;
import br.com.nlw.events.dto.SubscriptionRankingItem;
import br.com.nlw.events.dto.SubscriptionResponse;
import br.com.nlw.events.exception.EventNotFoundException;
import br.com.nlw.events.exception.SubscriptionConflictException;
import br.com.nlw.events.exception.UserReferrerNotFoundException;
import br.com.nlw.events.model.Event;
import br.com.nlw.events.model.Subscription;
import br.com.nlw.events.model.User;
import br.com.nlw.events.repository.EventRepository;
import br.com.nlw.events.repository.SubscriptionRepository;
import br.com.nlw.events.repository.UserRepository;

@Service
public class SubscriptionService {
	
	@Autowired
	private EventRepository eventRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private SubscriptionRepository subRepo;
	
	public SubscriptionResponse createNewSubscription(String eventName, User user, Integer userId) {
	
		Event evt = eventRepo.findByPrettyName(eventName);
		
		if(evt == null) throw new EventNotFoundException("Evento " + eventName + " não existe");
		
		User userRec = userRepo.findByEmail(user.getEmail());
		
		if(userRec == null) userRec = userRepo.save(user);
		
		User referrer = null;
		
		if(userId != null) {		
			referrer = userRepo.findById(userId).orElse(null);
			
			if(referrer == null) throw new UserReferrerNotFoundException("O usuário indicante com id " + userId + " não foi encontrado.");			
		}
		
		
		Subscription tmpSub = subRepo.findByEventAndSubscriber(evt, userRec);
		
		if(tmpSub != null) throw new SubscriptionConflictException("Já existe inscrição para o usúario " + userRec.getName() + " no evento " + evt.getPrettyName());
		
		Subscription subs = new Subscription();		
		subs.setEvent(evt);
		subs.setSubscriber(userRec);
		subs.setIndication(referrer);
		
		Subscription res = subRepo.save(subs);
		Integer id = res.getSubscriptionNumber();
		String url = "http://codecraft.com/subscription/" + res.getEvent().getPrettyName() + "/" + res.getSubscriber().getId();
				
		return new SubscriptionResponse(id, url);
	}
	
	public List<SubscriptionRankingItem> getFullRank(String prettyName) {
		Event evt = eventRepo.findByPrettyName(prettyName);
		
		if(evt == null) throw new EventNotFoundException("Evento" + prettyName + " não existe");
		
		return subRepo.generateRank(evt.getEventId());
	}
	
	public SubscriptionRankingByUser getRankById(String prettyName, Integer userId) {
		List<SubscriptionRankingItem> rankList = getFullRank(prettyName);
		
		SubscriptionRankingItem item = rankList.stream().filter(obj->obj.userId().equals(userId)).findFirst().orElse(null);
		
		if(item == null) throw new UserReferrerNotFoundException("Não há inscrições com indicação do usúario " + userId);
		
		Integer placement = IntStream.range(0, rankList.size())
							.filter(pl -> rankList.get(pl).userId().equals(userId))
							.findFirst().getAsInt();
		
		return new SubscriptionRankingByUser(item, placement+1);
	}
}
