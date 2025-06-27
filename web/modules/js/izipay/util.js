export const getDataOrderDynamic = () => {
    const currentTimeUnix = Date.now();
    const transactionId = currentTimeUnix.toString();
    const orderNumber = currentTimeUnix.toString().slice(0, 10);
    return {
        currentTimeUnix,
        transactionId,
        orderNumber,
    };
};