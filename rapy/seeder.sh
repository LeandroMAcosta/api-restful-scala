rm ./db/*.json
echo "Creando locations"
http localhost:4000/api/locations/ name="nueva cordoba" coordX:=11 coordY:=2000020
http localhost:4000/api/locations/ name="alberdi" coordX:=10 coordY:=2000020
http localhost:4000/api/locations/ name="guemes" coordX:=9 coordY:=2000020

echo "Creando providers"
http localhost:4000/api/providers/ username="lapeti1" storeName="nombre local" location="guemes" maxDeliveryDistance:=1000
http localhost:4000/api/providers/ username="lapeti2" storeName="local de guemes" location="guemes" maxDeliveryDistance:=1000
http localhost:4000/api/providers/ username="lapeti3" storeName="nombre xdd" location="nueva cordoba" maxDeliveryDistance:=1000

echo "Creando consumers"
http localhost:4000/api/consumers username="consumidor" location="guemes" 
http localhost:4000/api/consumers username="consumidor1" location="guemes" 
http localhost:4000/api/consumers username="consumidor2" location="guemes" 
http localhost:4000/api/consumers username="consumidor3" location="nueva cordoba" 
# name: string, description: string, price: float, providerUsername: string}
echo "Creando items"
http localhost:4000/api/items/ name="hamburguesas con papas" description="description1" price:=10 providerUsername="lapeti1"
http localhost:4000/api/items/ name="papas fritas" description="description2" price:=100 providerUsername="lapeti1"
http localhost:4000/api/items/ name="milanesa" description="description13" price:=30 providerUsername="lapeti2"
http localhost:4000/api/items/ name="fideos blancos" description="nueva cordoba" price:=6000 providerUsername="lapeti3"


echo "Get de providers"
http GET localhost:4000/api/providers
echo "Get de providers (guemes)"
http GET localhost:4000/api/providers?locationName="guemes"

echo "Get de consumers"
http localhost:4000/api/consumers

# http localhost:4000/api/orders providerUsername="lapeti1" consumerUsername="consumidor" items="asdasd"
echo "Post de orders"
http POST localhost:4000/api/orders providerUsername="lapeti1" consumerUsername="consumidor" items:='[{"name":"hamburguesas con papas","amount":2},{"name":"papas fritas","amount":1}]'

http POST localhost:4000/api/orders providerUsername="lapeti3" consumerUsername="consumidor" items:='[{"name":"fideos blancos","amount":4},{"name":"papas fritas","amount":3}]'

# rm ./db/*.json
http GET localhost:4000/api/orders?username="consumidor"

# chequear balances
http localhost:4000/api/providers?locationName="guemes"
