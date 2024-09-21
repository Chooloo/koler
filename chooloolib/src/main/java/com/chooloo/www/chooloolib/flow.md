New call
1. wrap it
2. Its state is incoming/call_waiting? 
   - yes -> notify state is incoming
   - no  -> update call

Call activity started
1. if the only call is disconnected, show an error
   - if call doesnt have account handle and it isnt a conference call, cause is "no accounts"
2. if has call (active/hold), call dismissKeyguard, if doesnt have an active call, allow keyguard