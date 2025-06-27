import { GetTokenSession } from './getTokenSession.js';
import { getDataOrderDynamic } from './util.js';

const { transactionId, orderNumber } = getDataOrderDynamic();
const MERCHANT_CODE = '4001834';
const PUBLIC_KEY = 'VErethUtraQuxas57wuMuquprADrAHAb';
const ORDER_CURRENCY = 'PEN';

// Función robusta para obtener el monto actual del input
function getOrderAmount() {
    const val = document.getElementById('totalPagarFinal').value;
    if (!val) return "0.00";
    const match = val.match(/([\d.,]+)/);
    if (!match) return "0.00";
    // Elimina separadores de miles (comas) y deja el punto decimal
    let num = match[1].replace(/,/g, '');
    return parseFloat(num).toFixed(2);
}

document.addEventListener('DOMContentLoaded', () => {
    const buttonPay = document.querySelector('#btnPayNow');
    buttonPay.disabled = false;
    buttonPay.innerHTML = "Pagar";

    buttonPay.addEventListener('click', async (event) => {
        event.preventDefault();

        // 1. Obtén el monto ACTUAL al hacer click
        const ORDER_AMOUNT = getOrderAmount();

        // 2. Verifica que el monto sea válido
        if (ORDER_AMOUNT === "0.00") {
            alert("El monto es cero. No puedes pagar.");
            return;
        }

        // 3. Solicita el token con el monto actual
        const authorization = await GetTokenSession(transactionId, {
            requestSource: 'ECOMMERCE',
            merchantCode: MERCHANT_CODE,
            orderNumber: orderNumber,
            publicKey: PUBLIC_KEY,
            amount: ORDER_AMOUNT,
        });

        const { response: { token = undefined, error } = {} } = authorization;

        if (!!token) {
            buttonPay.innerHTML = `S/. ${ORDER_AMOUNT} →`;

            const iziConfig = {
                config: {
                    transactionId,
                    action: Izipay.enums.payActions.PAY,
                    merchantCode: MERCHANT_CODE,
                    order: {
                        orderNumber,
                        currency: ORDER_CURRENCY,
                        amount: ORDER_AMOUNT,
                        processType: Izipay.enums.processType.AUTHORIZATION,
                        merchantBuyerId: 'mc1768',
                        dateTimeTransaction: Date.now().toString(),
                        payMethod: Izipay.enums.showMethods.ALL,
                    },
                    billing: {
                        firstName: 'Juan',
                        lastName: 'Wick',
                        email: 'jwick@izipay.pe',
                        phoneNumber: '989339999',
                        street: 'calle el demo',
                        city: 'lima',
                        state: 'lima',
                        country: 'PE',
                        postalCode: '00001',
                        document: '12345678',
                        documentType: Izipay.enums.documentType.DNI,
                    },
                    render: {
                        typeForm: Izipay.enums.typeForm.POP_UP,
                        container: '#your-iframe-payment',
                        showButtonProcessForm: false,
                    },
                    urlRedirect: 'https://server.punto-web.com/comercio/creceivedemo.asp?p=h1',
                    appearance: {
                        logo: 'https://logowik.com/content/uploads/images/shopping-cart5929.jpg',
                    },
                },
            };

            const callbackResponsePayment = response =>
                document.querySelector('#payment-message').innerHTML = JSON.stringify(response, null, 2);

            try {
                const checkout = new Izipay({ config: iziConfig.config });
                checkout && checkout.LoadForm({
                    authorization: token,
                    keyRSA: 'RSA',
                    callbackResponse: callbackResponsePayment,
                });
            } catch (error) {
                console.log(error.message, error.Errors, error.date);
            }
        } else {
            alert("Error al obtener el token, inténtalo de nuevo.");
        }
    });
});