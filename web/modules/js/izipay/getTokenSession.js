export async function GetTokenSession(transactionId, {
    requestSource = 'ECOMMERCE',
    merchantCode = '',
    orderNumber = '',
    publicKey = '',
    amount = '',
}) {
    try {
        const response = await fetch('http://localhost:8080/ecommerce_LP1_2025/TokenServlet', { // Cambia a tu puerto Node
            method: 'POST',
            headers: { 'Content-Type': 'application/json', 'transactionId': transactionId },
            body: JSON.stringify({
                requestSource,
                merchantCode,
                orderNumber,
                publicKey,
                amount,
            }),
        });
        return await response.json();
    } catch (e) {
        console.log('REVISA QUE EL ARCHIVO SERVER SE ESTE EJECUTANDO!');
        return {
            response: {
                token: undefined,
                error: '01_NODE_API'
            }
        };
    }
}