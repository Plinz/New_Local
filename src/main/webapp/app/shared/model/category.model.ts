import { IImage } from 'app/shared/model/image.model';

export interface ICategory {
    id?: number;
    name?: string;
    description?: string;
    imageId?: number;
    categoryParentId?: number;
    image?: IImage;
}

export class Category implements ICategory {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public imageId?: number,
        public categoryParentId?: number,
        public image?: IImage
    ) {}
}
