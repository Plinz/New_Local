import { Moment } from 'moment';
import { IImage } from 'app/shared/model/image.model';

export interface IStock {
    id?: number;
    name?: string;
    description?: string;
    quantityInit?: number;
    quantityRemaining?: number;
    priceUnit?: number;
    onSaleDate?: Moment;
    expiryDate?: Moment;
    bio?: boolean;
    available?: boolean;
    imageId?: number;
    productTypeId?: number;
    holdingId?: number;
    sellerId?: number;
    warehouseId?: number;
    image?: IImage;
}

export class Stock implements IStock {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public quantityInit?: number,
        public quantityRemaining?: number,
        public priceUnit?: number,
        public onSaleDate?: Moment,
        public expiryDate?: Moment,
        public bio?: boolean,
        public available?: boolean,
        public imageId?: number,
        public productTypeId?: number,
        public holdingId?: number,
        public sellerId?: number,
        public warehouseId?: number,
        public image?: IImage
    ) {
        this.bio = this.bio || false;
        this.available = this.available || false;
    }
}
