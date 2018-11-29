import { IStock } from 'app/shared/model//stock.model';
import { ICategory } from 'app/shared/model//category.model';
import { IProductType } from 'app/shared/model//product-type.model';
import { IHolding } from 'app/shared/model//holding.model';
import { IWarehouse } from 'app/shared/model//warehouse.model';

export interface IImage {
    id?: number;
    name?: string;
    description?: string;
    imageContentType?: string;
    image?: any;
    stocks?: IStock[];
    categories?: ICategory[];
    productTypes?: IProductType[];
    holdings?: IHolding[];
    warehouses?: IWarehouse[];
}

export class Image implements IImage {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public imageContentType?: string,
        public image?: any,
        public stocks?: IStock[],
        public categories?: ICategory[],
        public productTypes?: IProductType[],
        public holdings?: IHolding[],
        public warehouses?: IWarehouse[]
    ) {}
}
