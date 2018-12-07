import { IImage } from 'app/shared/model/image.model';

export interface IProductType {
    id?: number;
    name?: string;
    description?: string;
    imageId?: number;
    categoryId?: number;
    image?: IImage;
}

export class ProductType implements IProductType {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public imageId?: number,
        public categoryId?: number,
        public image?: IImage
    ) {}
}
