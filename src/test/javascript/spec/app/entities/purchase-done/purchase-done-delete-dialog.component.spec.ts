/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { NewLocalTestModule } from '../../../test.module';
import { PurchaseDoneDeleteDialogComponent } from 'app/entities/purchase-done/purchase-done-delete-dialog.component';
import { PurchaseDoneService } from 'app/entities/purchase-done/purchase-done.service';

describe('Component Tests', () => {
    describe('PurchaseDone Management Delete Component', () => {
        let comp: PurchaseDoneDeleteDialogComponent;
        let fixture: ComponentFixture<PurchaseDoneDeleteDialogComponent>;
        let service: PurchaseDoneService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NewLocalTestModule],
                declarations: [PurchaseDoneDeleteDialogComponent]
            })
                .overrideTemplate(PurchaseDoneDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(PurchaseDoneDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PurchaseDoneService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it(
                'Should call delete service on confirmDelete',
                inject(
                    [],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });
});
